docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-stable zap-api-scan.py \
-t http://192.168.178.116:9090/v3/api-docs -f openapi \
-z "-configfile /zap/wrk/options.prop" \
-D 20 \
-r zap_api_report.html \
-x report_xml.xml \
--hook=/zap/wrk/scan-policy.py
