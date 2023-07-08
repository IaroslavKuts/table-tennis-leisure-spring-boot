package com.example.demo.util;

import com.example.demo.domain.entity.AppOrder;
import com.example.demo.domain.entity.AppUser;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

// not in use
@RequiredArgsConstructor
@Component
public class LinksAssistant {

    private final String ORDER = "order";
    private final @Nonnull RepositoryEntityLinks entityLinks;

    public Iterable<Link> getOrderLinks(AppOrder appOrder) {

        LinkBuilder orderLinkBuilder = entityLinks.linkForItemResource(AppOrder.class, appOrder.getOrder_id());
        Link appUserLink = entityLinks.linkForItemResource(AppUser.class, appOrder.getAppUser().getUser_id()).withRel(ORDER);

        return Arrays.asList(orderLinkBuilder.withSelfRel(),appUserLink);
    }

}
