package soundsystem;

import org.springframework.stereotype.Component;

@Component
public class SgtPeppers  implements CompactDisc {

	private String title = "Sgt Peppers";
	private String artis = "The Beatles";
	
	@Override
	public void play() {
		System.out.println("Palyin " + title + " - " + artis);
		
	}

}
