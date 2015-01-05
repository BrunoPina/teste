package br.com.tagme.gwt.commons.client.auth;

import com.google.gwt.event.shared.EventHandler;

public interface AppAuthEventHandler extends EventHandler {
    void onAppAuthChange(AppAuthEvent event);
}