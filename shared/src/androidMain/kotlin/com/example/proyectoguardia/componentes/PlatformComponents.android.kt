package com.example.proyectoguardia.componentes

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun MapView(
    modifier: Modifier,
    isReportingMode: Boolean,
    isPlacementActive: Boolean,
    reportType: String,
    onPageFinished: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                
                webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        onPageFinished()
                        // Sincronizar estado inicial
                        val finalType = if (isPlacementActive) "'$reportType'" else "null"
                        view?.evaluateJavascript("toggleDrawing($isReportingMode, $finalType)", null)
                    }

                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        return false 
                    }
                }

                settings.apply {
                    javaScriptEnabled = true
                    domStorageEnabled = true
                    setSupportZoom(true)
                    builtInZoomControls = true
                    displayZoomControls = false
                    useWideViewPort = true
                    loadWithOverviewMode = true
                    setGeolocationEnabled(true)
                    userAgentString = "LuminaApp/1.0 (com.example.proyectoguardia; contacto@lumina.com)"
                }

                webChromeClient = object : android.webkit.WebChromeClient() {
                    override fun onGeolocationPermissionsShowPrompt(
                        origin: String?,
                        callback: android.webkit.GeolocationPermissions.Callback?
                    ) {
                        callback?.invoke(origin, true, false)
                    }
                }

                addJavascriptInterface(object {
                    @android.webkit.JavascriptInterface
                    fun onDangerProximity() {
                        try {
                            com.example.proyectoguardia.servicios.getNotificationService().sendAlertNotification(
                                "¡ALERTA DE SEGURIDAD!",
                                "Estás cerca de una zona PELIGROSA. Procede con precaución."
                            )
                        } catch (e: Exception) {
                            println("ERROR NOTIFICACIÓN: " + e.message)
                        }
                    }

                    @android.webkit.JavascriptInterface
                    fun onReportPlaced() {
                        // Compose manejará esto a través de isPlacementActive
                    }
                }, "AndroidInterface")

                val htmlData = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Guardian Lumina Map</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.css" />
                    <script src="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.js" charset="utf-8"></script>
                    
                    <style>
                        body { margin: 0; padding: 0; }
                        #map { height: 100vh; width: 100vw; background: #fdf5e6; transition: border 0.3s ease; }
                        .leaflet-container { background: #fdf5e6; }
                        .leaflet-control-locate a { background-color: #FFB74D !important; color: white !important; border: none !important; border-radius: 50% !important; }
                        
                        @keyframes dangerPulse {
                            0% { transform: scale(1); opacity: 0.6; }
                            50% { transform: scale(1.15); opacity: 0.2; }
                            100% { transform: scale(1); opacity: 0.6; }
                        }
                        .danger-glow { animation: dangerPulse 3s infinite ease-in-out; pointer-events: none; }
                        
                        .edit-menu { background: white; padding: 10px; border-radius: 8px; text-align: center; }
                        .btn-delete { background: #F44336; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer; font-weight: bold; }
                    </style>
                </head>
                <body>
                    <div id="map"></div>
                    <script>
                        var map = L.map('map', { zoomControl: false, tap: true }).setView([20.0842, -98.3694], 15);
                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '© OSM' }).addTo(map);

                        var drawingMode = false;
                        var currentReportType = null;
                        var firstPoint = null;
                        var tempMarker = null;
                        var reportGroups = {};
                        var lastUserPos = null;
                        var notifiedZones = new Set(); // Rastreo de zonas ya notificadas

                        function removeReportGroup(groupId) {
                            if (reportGroups[groupId]) {
                                reportGroups[groupId].forEach(function(layer) { map.removeLayer(layer); });
                                delete reportGroups[groupId];
                                notifiedZones.delete(groupId); // Limpiar rastreo si se borra
                                saveLayers();
                                map.closePopup();
                            }
                        }

                        function addInteractivity(mainLayer, groupId, label, relatedLayers) {
                            mainLayer.on('click', function(e) {
                                // Solo permitir borrar si NO estamos en modo colocación
                                if (drawingMode && currentReportType === null) {
                                    L.popup()
                                        .setLatLng(e.latlng)
                                        .setContent('<div class="edit-menu"><b>' + label + '</b><br><br><button class="btn-delete" onclick="removeReportGroup(\'' + groupId + '\')">BORRAR</button></div>')
                                        .openOn(map);
                                    L.DomEvent.stopPropagation(e);
                                }
                            });

                            if (mainLayer instanceof L.CircleMarker) {
                                mainLayer.on('dblclick', function(e) {
                                    if (!drawingMode || currentReportType !== null) return;
                                    
                                    var dragMarker = L.marker(mainLayer.getLatLng(), { draggable: true }).addTo(map);
                                    mainLayer.setOpacity(0.3);
                                    if (relatedLayers) relatedLayers.forEach(function(l) { l.setOpacity(0.1); });

                                    dragMarker.on('drag', function(ev) {
                                        var newPos = ev.latlng;
                                        mainLayer.setLatLng(newPos);
                                        if (relatedLayers) {
                                            relatedLayers.forEach(function(l) { if (l.setLatLng) l.setLatLng(newPos); });
                                        }
                                    });

                                    dragMarker.on('dragend', function() {
                                        mainLayer.setOpacity(1);
                                        if (relatedLayers) relatedLayers.forEach(function(l) { l.setOpacity(l.reportType==='light'?0.25:0.35); });
                                        map.removeLayer(dragMarker);
                                        saveLayers();
                                        if (mainLayer.reportType === 'danger') checkProximity(lastUserPos);
                                    });
                                    L.DomEvent.stopPropagation(e);
                                });
                            }
                        }

                        function saveLayers() {
                            var data = [];
                            for (var id in reportGroups) {
                                var group = reportGroups[id];
                                var mainLayer = group[0]; 
                                data.push({
                                    id: id,
                                    type: mainLayer instanceof L.Polyline ? 'polyline' : 'marker',
                                    latlngs: mainLayer instanceof L.Polyline ? mainLayer.getLatLngs() : mainLayer.getLatLng(),
                                    options: mainLayer.options,
                                    reportType: mainLayer.reportType,
                                    label: mainLayer.label
                                });
                            }
                            localStorage.setItem('lumina_data_final_v5', JSON.stringify(data));
                        }

                        function loadLayers() {
                            var saved = localStorage.getItem('lumina_data_final_v5');
                            if (!saved) return;
                            var items = JSON.parse(saved);
                            items.forEach(function(item) {
                                createReport(item.type, item.latlngs, item.reportType, item.label, item.id, true);
                            });
                        }

                        function createReport(type, pos, reportType, label, id, isLoading) {
                            var groupId = id || 'g' + Date.now() + Math.random();
                            var layers = [];

                            if (reportType === 'light') {
                                var glow = L.polyline(pos, { color: '#FFF176', weight: 22, opacity: 0.25, lineJoin: 'round' }).addTo(map);
                                var main = L.polyline(pos, { color: '#FBC02D', weight: 6, opacity: 0.9 }).addTo(map);
                                main.reportType = reportType; main.label = label;
                                layers = [main, glow];
                                addInteractivity(main, groupId, label, [glow]);
                            } else if (reportType === 'danger') {
                                var glow = L.circleMarker(pos, { radius: 28, fillColor: "#F44336", color: "transparent", fillOpacity: 0.35, className: 'danger-glow' }).addTo(map);
                                var main = L.circleMarker(pos, { radius: 12, fillColor: "#D32F2F", color: "white", weight: 3, fillOpacity: 1 }).addTo(map);
                                main.reportType = reportType; main.label = label;
                                layers = [main, glow];
                                addInteractivity(main, groupId, label, [glow]);
                                if (!isLoading) checkProximity(lastUserPos);
                            } else {
                                var color = reportType === 'store' ? "#4CAF50" : "#2196F3";
                                var main = L.circleMarker(pos, { radius: 14, fillColor: color, color: "white", weight: 3, fillOpacity: 0.9 }).addTo(map);
                                main.reportType = reportType; main.label = label;
                                layers = [main];
                                addInteractivity(main, groupId, label);
                            }
                            reportGroups[groupId] = layers;
                            if (!isLoading) saveLayers();
                        }

                        loadLayers();

                        L.control.locate({ 
                            position: 'topright', 
                            keepCurrentZoomLevel: true, 
                            flyTo: true,
                            locateOptions: { enableHighAccuracy: true }
                        }).addTo(map);

                        map.on('locationfound', function(e) {
                            lastUserPos = e.latlng;
                            checkProximity(e.latlng);
                        });

                        function checkProximity(latlng) {
                            if (!latlng) return;
                            for (var id in reportGroups) {
                                var main = reportGroups[id][0];
                                if (main.reportType === 'danger') {
                                    var distance = latlng.distanceTo(main.getLatLng());
                                    if (distance < 100) {
                                        // Solo notificar si es la primera vez que entra al rango
                                        if (!notifiedZones.has(id)) {
                                            if (window.AndroidInterface) window.AndroidInterface.onDangerProximity();
                                            notifiedZones.add(id);
                                            console.log("Entrada a zona peligrosa: " + id);
                                        }
                                    } else {
                                        // Si sale del rango (más de 100m + margen de error pequeño), permitir volver a notificar después
                                        if (distance > 110) { 
                                            if (notifiedZones.has(id)) {
                                                notifiedZones.delete(id);
                                                console.log("Salida de zona peligrosa: " + id);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        map.on('click', function(e) {
                            if (!drawingMode || currentReportType === null) return;
                            if (currentReportType === 'light') {
                                if (!firstPoint) {
                                    firstPoint = e.latlng;
                                    tempMarker = L.circleMarker(firstPoint, { radius: 8, fillColor: "#FFD54F", color: "white", weight: 2 }).addTo(map);
                                } else {
                                    createReport('polyline', [firstPoint, e.latlng], 'light', 'Calle Iluminada');
                                    if (tempMarker) map.removeLayer(tempMarker);
                                    firstPoint = null;
                                }
                            } else {
                                var label = currentReportType === 'store' ? 'Tienda Abierta' : currentReportType === 'people' ? 'Gente en zona' : 'ZONA PELIGROSA';
                                createReport('marker', e.latlng, currentReportType, label);
                            }
                        });

                        window.toggleDrawing = function(active, type) {
                            drawingMode = active;
                            currentReportType = type; 
                            firstPoint = null;
                            if (tempMarker) { map.removeLayer(tempMarker); tempMarker = null; }
                            var mapDiv = document.getElementById('map');
                            if (active && type !== null) {
                                mapDiv.style.border = "6px solid " + (type==='light'?'#FFD54F':type==='store'?'#4CAF50':type==='people'?'#2196F3':'#F44336');
                            } else {
                                mapDiv.style.border = "none";
                            }
                        };
                    </script>
                </body>
                </html>
                """.trimIndent()

                loadDataWithBaseURL("https://www.openstreetmap.org", htmlData, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            val finalType = if (isPlacementActive) "'$reportType'" else "null"
            webView.evaluateJavascript("toggleDrawing($isReportingMode, $finalType)", null)
        }
    )
}
