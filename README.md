# Parrot Bay

This is a simple HTTP server that you can run on your media server for ingesting content directly from Pirate Bay to your local qBittorrent instance.  Instead of transferring files or jumping between tabs, it's a one-click operation.

## Running on your media server:

### 1. Install headless qBittorrent on your server. [Read the guide here](https://github.com/qbittorrent/qBittorrent/wiki/Running-qBittorrent-without-X-server-(WebUI-only,-systemd-service-set-up,-Ubuntu-15.04-or-newer))

Note:
- Be sure to configure it as a systemd service
- You should also install a VPN to protect your privacy

### 2. Download the application on your server

SSH into your server and begin.

First, install Java on the server if it's not already installed:
```bash
sudo apt-get install default-jre
``` 

Download the latest executable jar, and the config template:
```bash
curl -fL https://raw.githubusercontent.com/bjhham/prtbay/refs/heads/main/prtbay-server.jar
curl -fL -o settings.conf https://raw.githubusercontent.com/bjhham/prtbay/refs/heads/main/example.settings.conf
```

Now, edit the `example.settings.conf` file to match your qBittorrent settings.
```bash
nano settings.conf
```
^O to save and ^X to exit.

You should be able to run it now with:

```bash
java -jar prtbay-server.jar -config=settings.conf
```

Now visit https://yourserver:8092/ to make sure it's working.

You should see something like this:
![Screenshot of the app](docs/screenshot.png)

### 3. Add it to systemd as a service

Add a new service file to systemd:
```bash
sudo nano /etc/systemd/system/prtbay.service
```

Enter the following, with the install directory for your `WorkingDirectory`:
```
[Unit]
Description=Parrot Bay
After=network-online.target
Wants=network-online.target

[Service]
Type=simple
TimeoutStartSec=30
ExecStart=/usr/bin/java -jar prtbay-server.jar -config=settings.conf

# CHANGE THIS TO YOUR INSTALL DIRECTORY
WorkingDirectory=/home/bjhham

[Install]
WantedBy=multi-user.target
```

Now start the service:
```bash
sudo systemctl start myapp.service
```

You can check the logs with:
```bash
journalctl -u prtbay.service -b
```

Enjoy!
