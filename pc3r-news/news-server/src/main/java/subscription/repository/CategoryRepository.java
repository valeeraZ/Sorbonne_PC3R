package subscription.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import tools.database.HibernateUtil;
import subscription.entity.Category;

import java.util.List;
import java.util.Optional;

public class CategoryRepository {
    SessionFactory sessionFactory;

    public CategoryRepository() {
        this.sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Optional<Category> findCategoryById(int id_category){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Category category = (Category) session.get(Category.class, id_category);
        session.close();
        return Optional.ofNullable(category);
    }

    public List<Category> findAll(){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Category");
        List<Category> categories = query.getResultList();
        session.close();
        return categories;
    }
}
