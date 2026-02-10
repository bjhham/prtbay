# Parrot Bay

This is a simple HTTP server that you can run on your media server for ingesting content directly from Pirate Bay to your local qBittorrent instance.  Instead of transferring files or jumping between tabs, it's a one-click operation.

How it works:
1. Install headless qBittorrent on your server. [Read the guide here](https://github.com/qbittorrent/qBittorrent/wiki/Running-qBittorrent-without-X-server-(WebUI-only,-systemd-service-set-up,-Ubuntu-15.04-or-newer))
2. Install Parrot Bay on your media server
3. Access it at `http://<host>:8092`
4. Search for a title and click the "download" button. This will add it to your list of torrents automatically.