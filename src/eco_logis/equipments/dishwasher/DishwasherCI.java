package equipments.dishwasher;

import equipments.crypto_miner.CryptoMinerImplementationI;
import fr.sorbonne_u.components.interfaces.OfferedCI;
import fr.sorbonne_u.components.interfaces.RequiredCI;

public interface DishwasherCI
        extends CryptoMinerImplementationI, RequiredCI, OfferedCI
{

}
