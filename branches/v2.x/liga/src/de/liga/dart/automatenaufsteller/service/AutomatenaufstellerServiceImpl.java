package de.liga.dart.automatenaufsteller.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 03.11.2007, 13:51:06
 */
public class AutomatenaufstellerServiceImpl extends AbstractService
        implements AutomatenaufstellerService {

    public void saveAutomatenaufsteller(Automatenaufsteller obj) {
        validate(obj);
        save(obj);
    }
    private void validate(Automatenaufsteller obj) throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();

        if (StringUtils.isEmpty(obj.getAufstellerName())) {
            findings.add(new ValidationMessage(MISSING, obj, "AufstellerName"));
        }
        if(!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }

    public Automatenaufsteller findAutomatenaufstellerById(long id) {
        return findById(Automatenaufsteller.class, id);
    }

    public List<Automatenaufsteller> findAllAutomatenaufsteller() {
        Query query = getSession().createQuery(
                "select a from Automatenaufsteller a order by a.aufstellerName");
        query.setCacheable(true);
        return query.list();
    }

    public List<Automatenaufsteller> findAllAutomatenaufstellerByLiga(
            Liga liga) {
        Query query;
        if (liga == null) {
            query = getSession().createQuery(
                    "select a from Automatenaufsteller a where a.liga is null order by a.aufstellerName");
        } else {
            query = getSession().createQuery(
                    "select a from Automatenaufsteller a where a.liga=? order by a.aufstellerName");
            query.setParameter(0, liga);
        }
        query.setCacheable(true);
        return query.list();
    }

    public void deleteAutomatenaufsteller(Automatenaufsteller obj)
            throws DartException {
        delete(obj);
    }
}
