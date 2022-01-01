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

Dans le composant batterie, vous utilisez deux variables booléennes pour traiter les états en chargement/en
déchargement. Pour une meilleure lisibilité de vos programmes, il est préférable d’utiliser des énumérations pour
représenter l’état dès qu’il y a plus de deux états possibles.
=====> TODO si on a le temps

##############################

TODO !!!!!! ElectricMeterElectricityModel : production éolienne
Cohérence des unités de mesure : kwh  ? amperes ? (see OvenElectricityModel - currentConsumption)
bcp de pb au niveau d'electric meter.
Oven : consumption = a * goalTemperature + b
PB AVEC OVEN : goalTemperature qui met le bazar dans Oven + une autre classe avec les trucs comme :
```java
this.simulatorPlugin.triggerExternalEvent(
                    OvenStateModel.URI,
                    t -> new SwitchOnOven(t, 200));
```
HEM : dishwasher, electric meter
RunHEMSim : windturbine
wind turbine, mil : coupledModel ?...
hem : classes qu'on a copiées... remplacer ?