package de.sciesla.datapackage;

import de.sciesla.sender.Sender;
import java.io.PrintStream;

public class KickDataPackage extends DataPackage {
	public KickDataPackage(String reason) {
		super(reason);
	}

	public void onServer(Sender sender) {
	}

	public void onClient(Sender sender) {
		System.out.println(getString(0));
	}
}
