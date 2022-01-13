# Projet_ALASCA : Eco-Logis

Hugo GUERRIER 3970839
Emilie SIAU 3700323

### Equipements
- Consommation incontrôlable : four
- Consommation suspensible : mineur de crypto-monnaie
- Consommation planifiable : lave-vaisselle

### Unités de production
- Aléatoire : éolienne

### Unités de stockage
- Batterie


### Récap Audit1

Abandon du générateur XML
Finir les composants : dishwasher (départ différé etc), power bank (changer, pas besoin de faire Discharge), Electric Meter
Tests complets d'intégration

### Oral 1 :

Dans la mesure où le remplissage du réservoir de la génératrice d’électricité ne peut se faire que par un utilisateur,
je vous suggère pour l’étape 3 d’enlever la méthode du composant et de le gérer dans le modèle de simulation
par une action de l’utilisateur. 
=====> TODO

##############################

TODO : au dessus, récap de audit 1 et surtout oral 1 + voir fiche pdf dans le mail du prof
TODO !!!!!! ElectricMeterElectricityModel : production éolienne
ExternalWindModel : améliorer la simulation du vent
pas bien compris RunHEMSim, avec connections.put eventsink....
+ finir power bank dedans

Cohérence des unités de mesure : kwh  ? amperes ? (see OvenElectricityModel - currentConsumption)
bcp de pb au niveau d'electric meter.
Oven : consumption = a * goalTemperature + b
Oven : régler le thermostat, prendre modèle sur le chauffage
HEM : dishwasher, electric meter
RunHEMSim : windturbine
wind turbine, mil : coupledModel ?...
hem : classes qu'on a copiées... remplacer ?