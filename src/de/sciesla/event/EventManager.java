package de.sciesla.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class EventManager {

	private ArrayList<EventHandler> eventHandlers;
	
	public EventManager() {
		
		eventHandlers = new ArrayList<>();
	}
	
	public void registerEventHandler(EventHandler eventHandler) {
		
		this.eventHandlers.add(eventHandler);
	}
	
	public void callEvent(Event event, Runnable runnable) {
		
		new Thread(() -> {

            boolean cancelled = false;

            for(EventHandler eventHandler : eventHandlers)
                for(Method method : eventHandler.getClass().getMethods())
                    if(method.getParameterCount() == 1 &&
                       method.isAnnotationPresent(de.sciesla.annotations.Event.class) &&
                       method.getParameterTypes()[0].equals(event.getClass())) {

                        try {

                            method.invoke(eventHandler, event);
                            if(event.isCancelled())
                                cancelled = true;

                        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
					}

            if(!cancelled)
                runnable.run();

        }).start();
					
	}
}
