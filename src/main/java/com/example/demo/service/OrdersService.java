package com.example.demo.service;

import com.example.demo.DTO.OrdersBulkDTO;
import com.example.demo.domain.entity.*;
import com.example.demo.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private  final OrderRepository orderRepository;
    private final EntityManager entityManager;

    public List<AppOrder> createListOfOrders(OrdersBulkDTO ordersBulkDTO, AppUser appUser){
        return ordersBulkDTO.chosenTimePeriods().stream().map(timePeriod->{
            String[] start_end_time = timePeriod.split("-");
            AppOrder appOrder = new AppOrder();
            appOrder.setPayment(100);
            appOrder.setPayment_status(1);
            appOrder.setStart_time(start_end_time[0]);
            appOrder.setEnd_time(start_end_time[1]);
            appOrder.setDate_of_game(ordersBulkDTO.dateOfGame());
            appOrder.setAppUser(appUser);
            return appOrder;
        }).collect(Collectors.toList());
    }

    /**
     * select
     *         email
     *     from
     *         users
     *     join
     *         orders
     *             on users.user_id=orders.user_id
     *     where
     *         a2_0.date_of_game=? (next day)
     *     group by
     *         1
     *
     * @return List of emails to which reminder emails will be sent
     */
    public List<String> receiveEmailsForReminderMessagesTheDayBefore(){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cq = cb
                .createQuery(String.class);
        Root<AppUser> root = cq.from(AppUser.class);
        Join<AppUser, AppOrder> appOrderJoin = root.join(AppUser_.APP_ORDERS);
        cq.select(root.get(AppUser_.EMAIL));
        cq.where(cb.equal(
                appOrderJoin.get(AppOrder_.DATE_OF_GAME)
                ,LocalDate.now().plusDays(1).toString()
                )
        );
        cq.groupBy(root.get(AppUser_.EMAIL));
        TypedQuery<String> tq = entityManager.createQuery(cq);
        List<String> resultSet = tq.getResultList();

        return resultSet;

    }
}
