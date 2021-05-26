docker run -v $(pwd):/zap/wrk/:rw -t owasp/zap2docker-stable zap-baseline.py \
-t http://192.168.178.42:9090 \
-z "-configfile /zap/wrk/options.prop" \
-g config.prop \
-r zap_baseline_report.html
