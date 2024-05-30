package com.vpactually;

import com.vpactually.util.DependencyContainer;
import com.vpactually.util.Generated;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
@Generated
public class Listener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DependencyContainer.injectDependencies(sce.getServletContext());
    }
}
