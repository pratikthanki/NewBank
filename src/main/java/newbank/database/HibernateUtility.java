package newbank.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

// Adapted from https://www.javaguides.net/2019/11/hibernate-h2-database-example-tutorial.html
public class HibernateUtility {
    private final String configurationResourceName;
    private StandardServiceRegistry registry;
    private SessionFactory sessionFactory;

    private HibernateUtility(){
        //Future configuration for production
        this("hibernate.cfg.xml");
    }
    private HibernateUtility(String configurationResourceName){
        this.configurationResourceName = configurationResourceName;
    }
    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create registry
                registry = new StandardServiceRegistryBuilder()
                        .configure()
                        .configure(configurationResourceName)
                        .build();
                // Create MetadataSources
                MetadataSources sources = new MetadataSources(registry);
                // Create Metadata
                Metadata metadata = sources.getMetadataBuilder().build();
                // Create SessionFactory
                sessionFactory = metadata.getSessionFactoryBuilder().build();
            } catch (Exception e) {
                e.printStackTrace();
                if (registry != null) {
                    StandardServiceRegistryBuilder.destroy(registry);
                }
            }
        }
        return sessionFactory;
    }
    public void shutdown() {
        if (registry != null) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    //This will create a database that would persist to file
    public static HibernateUtility development(){
        return new HibernateUtility("hibernate-development.cfg.xml");
    }
    public static HibernateUtility test(){
        return new HibernateUtility("hibernate-test.cfg.xml");
    }
}
