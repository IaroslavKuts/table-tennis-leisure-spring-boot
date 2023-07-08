package com.example.demo.service;

import com.example.demo.domain.entity.*;
import com.example.demo.domain.entity.AppOrder;
import com.example.demo.DTO.UsersDataByPaymentDTO;
import jakarta.persistence.EntityManager;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminSqlService {

    private final EntityManager entityManager;

    /**  *
     *     select
     *         name_of_abonement,
     *         count(name_of_abonement)
     *     from
     *         users
     *     join
     *         abonements
     *             on abonements.id_abonement = users.abonement
     *     group by
     *         name_of_abonement*
     * @return List of users' abonements and amount of the latest
     */
//    Using Tuple causes
//    https://discourse.hibernate.org/t/mirgration-from-hibernate-5-6-15-to-hibernate-6-2-1-final-caused-java-lang-object-not-superclass-of-jakarta-persistence-tuple/7562
    public List<Pair> readCustomersAbonements(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb
                .createQuery(Pair.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, Abonement> abonementJoin = root.join(AppUser_.ABONEMENT);
        cq.groupBy(abonementJoin.get(Abonement_.NAME_OF_ABONEMENT));
        CompoundSelection cmpd =  cb.construct(Pair.class,
                abonementJoin.get(Abonement_.NAME_OF_ABONEMENT),
                cb.count(abonementJoin.get(Abonement_.NAME_OF_ABONEMENT))
        );
        cq.select(cmpd);
        TypedQuery<Pair> tq = entityManager.createQuery(cq);
        List<Pair> resultList = tq.getResultList();
        return resultList;

    }

    /**
     *     select
     *         (datediff(CURRENT_DATE,date_of_birth)/CAST(? as signed)),
     *         COUNT(date_of_birth)
     *     from
     *         persons
     *     group by
     *         date_of_birth
     *
     *
     * @return List of users' ages and amount of the latest
     */
    public List<Pair> readCustomersAges(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb
                .createQuery(Pair.class);
        Root<Person> root = cq.from(Person.class);

        cq.groupBy(root.get(Person_.DATE_OF_BIRTH));
        CompoundSelection cmpd =  cb.construct(Pair.class,
                cb.quot(cb.function("datediff",Number.class,cb.currentDate(),root.get(Person_.DATE_OF_BIRTH)),365),
                cb.count(root.get(Person_.DATE_OF_BIRTH))
        );
        cq.select(cmpd);

        TypedQuery<Pair> tq = entityManager.createQuery(cq);
        List<Pair> resultList = tq.getResultList();

        return resultList;

    }


    /**
     * select
     *         sum(payment),
     *         date_of_game
     *     from
     *         orders
     *     where
     *         date_of_game between fromDate and toDate
     *     group by
     *         date_of_game
     *
     *
     *
     *
     * @param fromDate - Optional, that contains start date in YYYY-MM-DD (supposedly) format
     * @param toDate - Optional, that contains end date in YYYY-MM-DD (supposedly) format
     * @return List of profits (if any) for each day between given dates
     */
    public List<Pair> readProfitBetweenDates(Optional<String> fromDate, Optional<String> toDate){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cq = cb
                .createQuery(Pair.class);
        Root<AppOrder> root = cq.from(AppOrder.class);
        cq.where(
                cb.between(
                        root.get(AppOrder_.DATE_OF_GAME),
                        fromDate.orElseGet(()-> LocalDate.now().toString()),
                        toDate.orElseGet(()->LocalDate.now().minusDays(30L).toString())
                )
        );
        cq.groupBy(root.get(AppOrder_.DATE_OF_GAME));
        CompoundSelection cmpd =  cb.construct(
                Pair.class,
                root.get(AppOrder_.DATE_OF_GAME),
                cb.sum(root.get(AppOrder_.payment))
        );
        cq.select(cmpd);

        TypedQuery<Pair> tq = entityManager.createQuery(cq);
        List<Pair> resultList = tq.getResultList();

        return resultList;

    }

    /**
     *     select
     *         count(date_of_game),
     *         date_of_game
     *     from
     *         orders
     *     where
     *         date_of_game between fromDate and toDate
     *     group by
     *         date_of_game
     *
     *
     * @param fromDate - Optional, that contains start date in YYYY-MM-DD (supposedly) format
     * @param toDate - Optional, that contains end date in YYYY-MM-DD (supposedly) format
     * @return List of days' load (if any) for each day between given dates
     */
    public List<Pair> readDaysLoadBetweenDays(Optional<String> fromDate, Optional<String> toDate){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cq = cb
                .createQuery(Pair.class);
        Root<AppOrder> root = cq.from(AppOrder.class);
        cq.where(
                cb.between(
                        root.get(AppOrder_.DATE_OF_GAME),
                        fromDate.orElseGet(()-> LocalDate.now().toString()),
                        toDate.orElseGet(()->LocalDate.now().minusDays(30L).toString())
                )
        );
        cq.groupBy(root.get(AppOrder_.DATE_OF_GAME));
        CompoundSelection cmpd =  cb.construct(
                Pair.class,
                root.get(AppOrder_.DATE_OF_GAME),
                cb.count(root.get(AppOrder_.DATE_OF_GAME))
        );
        cq.select(cmpd);

        TypedQuery<Pair> tq = entityManager.createQuery(cq);
        List<Pair> resultList = tq.getResultList();

        return resultList;

    }

    /**
     *     select
     *         sum(payment),
     *         email,
     *         first_name,
     *         surname
     *     from
     *         users
     *     join
     *         persons
     *             on users.user_id=persons.user_id
     *     join
     *         orders
     *             on users.user_id=orders.user_id
     *     where
     *         o1_0.date_of_game between fromDate and toDate
     *     group by
     *         a1_0.user_id
     *     having
     *         sum(o1_0.payment) between fromSum and toSum
     *
     *
     * @param fromDate - Optional, that contains start date in YYYY-MM-DD (supposedly) format
     * @param toDate - Optional, that contains end date in YYYY-MM-DD (supposedly) format
     * @param fromSum - Optional, that contains start sum
     * @param toSum - Optional, that contains end sum
     * @return List of users
     */
    public List<UsersDataByPaymentDTO> readUsersDataByPayment(
            Optional<String> fromDate,
            Optional<String> toDate,
            Optional<Integer> fromSum,
            Optional<Integer> toSum){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb
                .createQuery(UsersDataByPaymentDTO.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, Person> personJoin = root.join(AppUser_.PERSON);
        Join<AppUser, AppOrder> orderJoin = root.join(AppUser_.APP_ORDERS);
        CompoundSelection cmpd =  cb.construct(UsersDataByPaymentDTO.class,
                cb.sum(orderJoin.get(AppOrder_.PAYMENT)).alias("profit"),
                root.get(AppUser_.EMAIL).alias("email"),
                personJoin.get(Person_.FIRST_NAME).alias("first_name"),
                personJoin.get(Person_.SURNAME).alias("surname")

        );
        cq.select(cmpd);
        cq.where(
                cb.between(
                        orderJoin.get(AppOrder_.DATE_OF_GAME),
                        fromDate.orElseGet(()-> LocalDate.now().toString()),
                        toDate.orElseGet(()->LocalDate.now().minusDays(30L).toString())
                )
        );
        cq.groupBy(root.get(AppUser_.USER_ID));
        cq.having(
                cb.between(
                        cb.sum(orderJoin.get(AppOrder_.PAYMENT)),
                        fromSum.orElseGet(()-> 0),
                        toSum.orElseGet(()->Integer.MAX_VALUE)
        ));
        TypedQuery<UsersDataByPaymentDTO> tq = entityManager.createQuery(cq);
        List<UsersDataByPaymentDTO> resultList = tq.getResultList();
        return resultList;

    }


}
