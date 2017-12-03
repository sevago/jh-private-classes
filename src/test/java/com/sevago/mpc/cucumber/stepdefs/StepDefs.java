package com.sevago.mpc.cucumber.stepdefs;

import com.sevago.mpc.PrivateclassesApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = PrivateclassesApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
