package com.sevago.mpc.service.impl;

import com.sevago.mpc.config.ApplicationProperties;
import com.sevago.mpc.security.AuthoritiesConstants;
import com.sevago.mpc.security.SecurityUtils;
import com.sevago.mpc.service.InvoiceService;
import com.sevago.mpc.domain.Invoice;
import com.sevago.mpc.repository.InvoiceRepository;
import com.sevago.mpc.repository.search.InvoiceSearchRepository;
import com.sevago.mpc.service.dto.InvoiceDTO;
import com.sevago.mpc.service.mapper.InvoiceMapper;
import com.sevago.mpc.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Invoice.
 */
@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService{

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final InvoiceSearchRepository invoiceSearchRepository;

    private final ApplicationProperties applicationProperties;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceMapper invoiceMapper, InvoiceSearchRepository invoiceSearchRepository, ApplicationProperties applicationProperties) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.invoiceSearchRepository = invoiceSearchRepository;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a invoice.
     *
     * @param invoiceDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        log.debug("Request to save Invoice : {}", invoiceDTO);
        Invoice invoice = invoiceMapper.toEntity(invoiceDTO);
        invoice = invoiceRepository.save(invoice);
        InvoiceDTO result = invoiceMapper.toDto(invoice);
        if (applicationProperties.getElasticsearch().isEnabled()) {
            invoiceSearchRepository.save(invoice);
        }
        return result;
    }

    /**
     * Get all the invoices.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return Stream.of(AuthoritiesConstants.ADMIN)
            .map(authority -> {
                if(SecurityUtils.isCurrentUserInRole(authority)) {
                    return invoiceRepository.findAll(pageable);
                } else {
                    return invoiceRepository.findByTeachingInstructorUserLoginOrderByIssueDateDesc(SecurityUtils.getCurrentUserLogin().orElseThrow(() ->
                        new InternalServerErrorException("Current user login not found")), pageable);
                }
            })
            .map(page -> page.map(invoiceMapper::toDto))
            .findFirst()
            .orElseThrow(() -> new InternalServerErrorException("Missing Spring Data Page"));
    }

    /**
     * Get one invoice by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public InvoiceDTO findOne(Long id) {
        log.debug("Request to get Invoice : {}", id);
        Invoice invoice = invoiceRepository.findOneWithEagerRelationships(id);
        return invoiceMapper.toDto(invoice);
    }

    /**
     * Delete the invoice by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Invoice : {}", id);
        invoiceRepository.delete(id);
        if (applicationProperties.getElasticsearch().isEnabled()){
            invoiceSearchRepository.delete(id);
        }
    }

    /**
     * Search for the invoice corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<InvoiceDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Invoices for query {}", query);
        Page<Invoice> result = invoiceSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(invoiceMapper::toDto);
    }
}
