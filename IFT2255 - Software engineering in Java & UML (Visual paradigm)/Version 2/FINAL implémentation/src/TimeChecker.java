import java.util.ArrayList;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Classe timer qui s'occupe d'avertir le systeme quand faire la synthèse ou mettre à jour le status des clients
 */
class TimeChecker extends Thread {
	
	private static Clients clients;
	private static Services services;
	
	public void addData(Clients clients, Services services) {
		
		this.clients = clients;
		this.services = services;
	}
	
	public void run(){
		
		while(true) {
			
			try {
			
				LocalDateTime now = LocalDateTime.now();
				
				if ((now.getDayOfWeek().getValue() == 5) && (now.getHour() == 0) && (now.getMinute() == 0)) { 
					
					
					AutoProc.procComptable(this.clients, this.services);
					
					TimeUnit.MINUTES.sleep(60);
				}
				
				TimeUnit.SECONDS.sleep(30); // Check again in 30 seconds
			
			} catch(InterruptedException e) {
				;
			}
			
		}
		
	}
	
}