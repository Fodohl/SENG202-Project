package seng202.team1.gui.phases;

import seng202.team1.gui.Router;
import seng202.team1.gui.layout.Layout;
import seng202.team1.gui.pages.Pages;

import java.util.List;

public class AuthPhase extends Phase{


    public AuthPhase(Router router){
        super(router, Phases.AUTH, null);
    }

    @Override
    public List<Pages> allowedPages() {
        return List.of(
            Pages.LOGIN,
            Pages.RESET,
            Pages.SIGN_UP
        );
    }

    @Override
    public Pages entryPage() {
        return Pages.LOGIN;
    }

}
