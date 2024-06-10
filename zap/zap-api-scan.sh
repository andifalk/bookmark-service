docker run -v $(pwd):/zap/wrk/:rw -t zaproxy/zap-stable zap-api-scan.py \
-t http://192.168.178.20:9090/v3/api-docs -f openapi \
-z "-configfile /zap/wrk/options.prop" \
-D 20 \
-r zap_api_report.html \
-x report_xml.xml \
--hook=/zap/wrk/scan-policy.py
