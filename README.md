Azure Animal Directory - Technical Meetup 27/11/2025

Deze applicatie is gemaakt om te connecteren met een of meerdere functions binnen azure. 
Deze functions gebruiken hun eigen interne keuken voor het opzoeken via Artificiële Intelligentie. 
Dit is een Spring Boot API die gaat opzoeken als de permissies matchen en de resultaten opslaan voor cachingdoeleinden.

Achterliggend doet deze een get request naar de function(s) <br>
Input: url?code=xxx&animal=xxx <br>
Output: "data": [{ "image": "data:image/png;base64," + base64 }]

Hieronder zijn de instructies voor het deployment, lees eerst beide opties voor je vertrekt met een van beiden voor frustraties te vermijden.

Prerequisites:
- Opzetten van een Azure omgeving met subscription
- Opzetten van de Azure Functions (evt in DEV_MODE voor testing)
- Opzetten van de OpenAI Images (Optioneel)

(Indien je al een bestaande subscription gebruikt die NIET deel uitmaakt van de Free Tier) <br>
Stappenplan: <br>
1A: application.properties populeren met de juiste services. <br>
1B: pom.xml populeren met de juiste services. <br>
2: Maak het App Service Plan aan (als het nog niet bestaat) <br>
3: Run mvn -X clean package azure-webapp:deploy -DAZURE_RG=<resource-group> -DAZURE_APP=<app-name> -DAZURE_PLAN<service-plan> -DAZURE_PLAN_RG=<resource-group> <br>

(Indien je binnen de Free Tier wilt blijven maak je gebruik van Spring Container Apps) <br>
Stappenplan: <br>
1A: application.properties populeren met de juiste services. <br>
1B: pom.xml populeren met de juiste services. <br>
2: Maak een Azure Container Registry (Jenkins variant) <br>
3: Binnen de Azure CLI (Of lokaal na az login): az acr credential show --name <acr-name> <br>
3.1: Indien dit commando eerste maal faalt: az acr update -n <acr-name> --admin-enabled true <br>
4: Vul de credentials in de pom.xml <br>
5: mvn -X clean package compile jib:build (Dit pusht de lokale spring image naar de ACR) <br>
5.1: Kijken of de push geslaagd is: az acr repository list --name <acr-name> --output table <br>
6: Maak een Container App Environment (Verzameling van Containers voor communicatie evt.): az containerapp env create --name <env-name> --resource-group <resource-group> --location <region> <br>
7: Maak een Container App: az containerapp create --name <app-name> --resource-group <resource-group> --environment <env-name> --image <acr-name>.azurecr.io/<image-name> --target-port 8080 --ingress external --registry-server <acr-name>.azurecr.io --registry-username <acr-user> --registry-password <acr-pwd> <br>

Normaal kan je vanaf dan aan je spring boot applicatie, de url van de spring boot applicatie zie je binnen de Container App in de Azure Portal. Hier kan je ook rechtstreeks aan de logs.

Voor het verder automatiseren van updates: <br>
1: az containerapp update --name <app-name> --resource-group <resource-group> --image <acr-name>.azurecr.io/<image-name> --registry-server <acr-name>.azurecr.io --registry-username <acr-user> --registry-password <acr-pwd>

? Kan je gebruik maken van docker ? Ja, je kan ook je image uploaden naar hub.docker.io en daarmee connecteren. Deze oplossing is puur Azure gericht en zonder Docker. <br>
? Heb je Azure CLI nodig ? Nee, binnen het Azure Portaal heb je een terminal waarmee je alle commando's kan uitvoeren. <br>
? Hoe call ik mijn Spring boot App ? Alles is binnen handbereik als je naar je container-app gaat. De url is in overview te vinden, de logs binnen Application > Revisions & Replicas
