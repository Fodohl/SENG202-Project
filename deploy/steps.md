# Steps
- set  up a linux box with all the nice stuff like dokcer
- get a keypair and put it in DEPLOY_SSH_KEY on CI
- setup domain routing: (subdomain, service)
- - trash-app.maxtyson.net	        http://taserver:4374
- - trash-app.server.maxtyson.net	http://grafana:3000
- - deploy.maxtyson.net	            ssh://localhost:22
- 

# Cloudflare stuff if needed
- when domain routing can use tunnels to protect server ip
- should really use a service token on the deploy enpoint via zero trust