def zap_started(zap, target):
    zap.ascan.import_scan_policy("/zap/wrk/backend.policy")
    zap.ascan.set_option_default_policy("Spring Boot Backend")
    print("zap_started(target={})".format(target))

def zap_active_scan(zap, target, policy):
    print("zap_active_scan(target={}, policy={})".format(target, policy))
    zap.ascan.scan(url=target,scanpolicyname="Spring Boot Backend")

def zap_spider(zap, target):
    zap.ascan.import_scan_policy("/zap/wrk/backend.policy")
    zap.ascan.enable_all_scanners("Spring Boot Backend")
    zap.ascan.set_option_default_policy("Spring Boot Backend")
    print("zap_spider(target={})".format(target))
