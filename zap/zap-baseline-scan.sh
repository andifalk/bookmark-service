docker run -v $(pwd):/zap/wrk/:rw -t zaproxy/zap-stable zap-baseline.py \
-t http://172.16.177.199:9090 \
-z "-configfile /zap/wrk/options.prop" \
-g config.prop \
-r zap_baseline_report.html
