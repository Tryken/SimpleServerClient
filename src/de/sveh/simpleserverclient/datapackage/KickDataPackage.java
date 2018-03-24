package de.sveh.simpleserverclient.datapackage;

import de.sveh.simpleserverclient.sender.Sender;

public class KickDataPackage extends AbstractDataPackage {

    public KickDataPackage(String reason) {
        super(reason);
    }

    public void onServer(Sender sender) {
    }

    public void onClient(Sender sender) {
        System.out.println(getString(0));
    }
}
