#!/bin/bash

APP_DIR_NAME=SortingVisualizer.app

javapackager \
  -deploy -Bruntime="/Library/Java/JavaVirtualMachines/jdk-11.0.9.jdk/Contents/Home" \
  -native image \
  -srcdir . \
  -srcfiles MyVisualizer.jar \
  -srcfiles buttons \
  -width 1028 -height 720 \
  -outdir release \
  -outfile ${APP_DIR_NAME} \
  -appclass MainFrame \
  -name "Sorting Visualizer" \
  -title "Sorting Visualizer" \
  -Bicon=app_icon.icns \
  -nosign \
  -v

echo ""
echo "If that succeeded, it created \"release/bundles/${APP_DIR_NAME}\""
