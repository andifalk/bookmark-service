docker run -v $(pwd):/zap/wrk/:rw -t zaproxy/zap-stable zap-full-scan.py \
-t http://192.168.178.42:9090/api \
-n /zap/wrk/bookmark-service.context \
-U 'Bruce Wayne' \
-z "-configfile /zap/wrk/options.prop" \
-D 10 \
-r zap_full_report.html \
--hook=/zap/wrk/scan-policy.py
