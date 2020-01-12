import java.awt.EventQueue;
public class MainClass {
    
    public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
                                        
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
	}	
    
}
