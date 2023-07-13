import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 * Classe pro. avec Client comme parent. Permet de distinguer un client à un membre. A sa propre fonction getIsPro()
 * overridé.
 */
public class Professionel extends Client {
	
	Professionel(int numeroClient, String nom, String prenom, LocalDateTime date, boolean isSus, String address, String town, String postCode,String courriel)  {
		super(numeroClient, nom, prenom, date, isSus, address, town, postCode,courriel);
	}
	
	@Override
	public boolean getIsPro() {return true;}
	
}