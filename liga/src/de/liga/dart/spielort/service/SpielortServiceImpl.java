package de.liga.dart.spielort.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import static de.liga.dart.exception.ValidationReason.WRONG;
import de.liga.dart.model.Automatenaufsteller;
import de.liga.dart.model.Liga;
import de.liga.dart.model.Spielort;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:34:54
 */
public class SpielortServiceImpl extends AbstractService
        implements SpielortService {
    public void saveSpielort(Spielort obj) {
        validate(obj);
        save(obj);
    }

    private void validate(Spielort spielort) throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();
        /* da manche aufsteller zu mehreren ligen gehören können (To-Many-Beziehung wäre besser)
        if (spielort.getLiga() != null && spielort.getAutomatenaufsteller() != null
                && spielort.getAutomatenaufsteller().getLiga() != null) {
            if (spielort.getLiga().getLigaId() != spielort.getAutomatenaufsteller().getLiga().getLigaId()) {
              findings.add(new ValidationMessage(WRONG, spielort, "Liga passt nicht zur Liga des Aufstellers"));
            }
        }   */
        if (StringUtils.isEmpty(spielort.getSpielortName())) {
            findings.add(
                    new ValidationMessage(MISSING, spielort, "SpielortName"));
        }
        if (spielort.getAutomatenAnzahl() < 0) {
            findings.add(
                    new ValidationMessage(WRONG, spielort, "AutomatenAnzahl"));
        }
        if (spielort.getFreierTag() != null) {
            if (spielort.getFreierTag() < Calendar.SUNDAY ||
                    spielort.getFreierTag() > Calendar.SATURDAY) {
                findings.add(
                        new ValidationMessage(WRONG, spielort, "FreierTag"));
            }
        }
        if (!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }

    public Spielort findSpielortById(long id) {
        return findById(Spielort.class, id);
    }

    public List<Spielort> findAllSpielort() {
        Query query = getSession().createQuery(
                "select s from Spielort s order by s.spielortName");
        query.setCacheable(true);
        return query.list();
    }

    public List<Spielort> findAllSpielortByLiga(Liga liga) {

        if (liga == null) {
            return findAllSpielort();
        } else {
            Query query;
            query = getSession().createQuery(
                    "select s from Spielort s where s.liga=? order by s.spielortName");
            query.setParameter(0, liga);
            return query.list();
        }
    }

    public List<Spielort> findAllSpielortByLigaAufsteller(Liga liga,
                                                          Automatenaufsteller aufsteller) {
        if (aufsteller == null) return findAllSpielortByLiga(liga);
        else if (liga != null) {
            Query query;
            query = getSession().createQuery(
                    "select s from Spielort s where s.liga = :liga and s.automatenaufsteller = :aufsteller order by s.spielortName");
            query.setParameter("liga", liga);
            query.setParameter("aufsteller", aufsteller);
            return query.list();
        } else {
            Query query;
            query = getSession().createQuery(
                    "select s from Spielort s where s.automatenaufsteller = :aufsteller order by s.spielortName");
            query.setParameter("aufsteller", aufsteller);
            return query.list();
        }
    }

    public void deleteSpielort(Spielort obj) throws DartException {
        delete(obj);
    }
}
