package com.example.proyectoguardia

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
                        // Ensure drawing mode is in sync when page loads
                        view?.evaluateJavascript("toggleDrawing($isReportingMode)", null)
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
                    setGeolocationEnabled(true) // Permite geolocalización
                    userAgentString = "LuminaApp/1.0 (com.example.proyectoguardia; contacto@lumina.com)"
                }

                // Permisos de geolocalización para el WebView
                webChromeClient = object : android.webkit.WebChromeClient() {
                    override fun onGeolocationPermissionsShowPrompt(
                        origin: String?,
                        callback: android.webkit.GeolocationPermissions.Callback?
                    ) {
                        callback?.invoke(origin, true, false)
                    }
                }

                val htmlData = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Lumina Map</title>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
                    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
                    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
                    
                    <!-- Plugin para localización (Similar a Google Maps) -->
                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.css" />
                    <script src="https://cdn.jsdelivr.net/npm/leaflet.locatecontrol/dist/L.Control.Locate.min.js" charset="utf-8"></script>
                    
                    <style>
                        body { margin: 0; padding: 0; }
                        #map { height: 100vh; width: 100vw; background: #fdf5e6; }
                        .leaflet-container { background: #fdf5e6; }
                        /* Personalización del botón de ubicación. */
                        .leaflet-control-locate a {
                            background-color: #FFB74D !important;
                            color: white !important;
                        }
                    </style>
                </head>
                <body>
                    <div id="map"></div>
                    <script>
                        var map = L.map('map', {
                            zoomControl: false // Quitamos controles por defecto para que sea más limpio
                        }).setView([20.0842, -98.3694], 15);

                        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                            attribution: '© OSM'
                        }).addTo(map);

                        // Agregar control de ubicación
                        var lc = L.control.locate({
                            position: 'topright',
                            strings: {
                                title: "Muéstrame dónde estoy"
                            },
                            locateOptions: {
                                enableHighAccuracy: true
                            },
                            keepCurrentZoomLevel: true,
                            flyTo: true
                        }).addTo(map);

                        // Iniciar localización automáticamente
                        lc.start();

                        var drawingMode = false;
                        var firstPoint = null;
                        var tempMarker = null;
                        
                        function setDrawingMode(active) {
                            drawingMode = active;
                            firstPoint = null;
                            if (tempMarker) {
                                map.removeLayer(tempMarker);
                                tempMarker = null;
                            }
                            // Cambiar cursor para indicar modo dibujo
                            document.getElementById('map').style.cursor = active ? 'crosshair' : '';
                        }

                        map.on('click', function(e) {
                            console.log("Click en mapa: " + drawingMode);
                            if (!drawingMode) return;

                            if (!firstPoint) {
                                firstPoint = e.latlng;
                                tempMarker = L.circleMarker(firstPoint, {
                                    radius: 8,
                                    fillColor: "#FFD54F",
                                    color: "#FF8F00",
                                    weight: 2,
                                    opacity: 1,
                                    fillOpacity: 0.8
                                }).addTo(map);
                            } else {
                                var secondPoint = e.latlng;
                                
                                // Dibujar la franja amarilla semitransparente
                                L.polyline([firstPoint, secondPoint], {
                                    color: '#FFF176',
                                    weight: 12,
                                    opacity: 0.6,
                                    lineJoin: 'round'
                                }).addTo(map);

                                // Línea de borde para mejor visibilidad (Efecto brillo)
                                L.polyline([firstPoint, secondPoint], {
                                    color: '#FBC02D',
                                    weight: 3,
                                    opacity: 0.9
                                }).addTo(map);

                                // Reset para el siguiente tramo (deja marcar múltiples tramos)
                                if (tempMarker) map.removeLayer(tempMarker);
                                
                                // Opcional: El segundo punto se vuelve el primero para continuar la línea
                                // firstPoint = secondPoint; 
                                // tempMarker = L.circleMarker(firstPoint, {...}).addTo(map);
                                
                                // Para este requerimiento, parece que quieren 2 puntos y ya
                                firstPoint = null;
                                tempMarker = null;
                            }
                        });
                        
                        window.toggleDrawing = function(active) {
                            console.log("Toggle drawing: " + active);
                            setDrawingMode(active);
                        };
                    </script>
                </body>
                </html>
                """.trimIndent()

                loadDataWithBaseURL("https://www.openstreetmap.org", htmlData, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            // Actualizar el modo de dibujo cuando cambia el estado de Compose
            webView.evaluateJavascript("toggleDrawing($isReportingMode)", null)
        }
    )
}
