package de.liga.dart.common.service;

import de.liga.dart.model.Automatenaufsteller;

import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 04.11.2007, 08:10:10
 */
public interface TestDummyService extends Service{
    Automatenaufsteller findAutomatenaufstellereById(int i);

    List<Automatenaufsteller> findAllAutomatenaufsteller();
}
