# Run OWASP Zap Docker

## Standard image (amd64)

Pull the image using `docker pull owasp/zap2docker-stable`.

## Apple M1 (arm64)

To use it using arm64 chip the docker image has to be build ourselves as the standard image is incompatible and won't run.

To build it

1. Clone the repository https://github.com/zaproxy/zaproxy.git
2. Change directory to `zaproxy`
3. Run `docker build -f Dockerfile-stable .`
4. Check image id for image just build using `docker images`
5. Tag the image using `docker tag <image-id> owasp/zap2docker-stable:latest` (replace image-id with correct one)

## Run Zap Scan

### Baseline Scan

Run a fast basic passive scan with these steps:

1. Check your local ip address (`localhost` is not possible using docker)
2. Locate and adapt ip address in `zap-baseline-scan.sh` script
3. Run the bookmark application and check if you can access the application using http://your-ip:9090/v3/api-docs (replace with your local ip address)
4. Run `./zap-baseline-scan.sh`
5. Wait and see the scan results

### API Scan

Run an active scan for the bookmarks API by importing the open api specification doc with these steps:

1. Check your local ip address (`localhost` is not possible using docker)
2. Locate and adapt ip address in `zap-api-scan.sh` script
3. Run the bookmark application and check if you can access the application using http://your-ip:9090/v3/api-docs (replace with your local ip address)
4. Run `./zap-api-scan.sh`
5. Wait and see the scan results

### Full Scan

Run a full active scan for the bookmarks application with these steps:

1. Check your local ip address (`localhost` is not possible using docker)
2. Locate and adapt ip address in `zap-full-scan.sh` script
3. Run the bookmark application and check if you can access the application using http://your-ip:9090/v3/api-docs (replace with your local ip address)
4. Run `./zap-full-scan.sh`
5. Wait and see the scan results