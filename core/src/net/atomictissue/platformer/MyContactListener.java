package net.atomictissue.platformer;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().getUserData().equals("foot") || contact.getFixtureB().getUserData().equals("foot")) {
            TheGame.footContacts++;
        }
    }

    @Override
    public void endContact(Contact contact) {
        if(contact.getFixtureA().getUserData().equals("foot") || contact.getFixtureB().getUserData().equals("foot")) {
            TheGame.footContacts--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
