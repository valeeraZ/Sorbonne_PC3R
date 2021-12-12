package user.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import tools.database.HibernateUtil;
import user.entity.User;

import java.util.Optional;

public class UserRepository {
    SessionFactory sessionFactory;

    public UserRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void save(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        String id = (String) session.save(user);
        session.getTransaction().commit();
        session.close();
        //return id;
    }

    public void update(User user){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();
        session.close();
    }

    public Optional<User> getUserByEmail(String email){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from User u where u.email = ?1");
        query.setParameter(1, email);
        User user = (User) query.uniqueResult();
        session.close();
        return Optional.ofNullable(user);
    }

    public Optional<User> getUserById(String id){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = (User) session.get(User.class, id);
        session.close();
        return Optional.ofNullable(user);
    }

}
