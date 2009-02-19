package de.liga.dart.ligaklasse.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import de.liga.dart.model.Ligaklasse;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:   <br/>
 * User: roman
 * Date: 02.11.2007, 13:04:51
 */
public class LigaklasseServiceImpl extends AbstractService
        implements LigaklasseService {
    public void saveLigaklasse(Ligaklasse aLigaklasse) {
        validate(aLigaklasse);
        save(aLigaklasse);
    }

    private void validate(Ligaklasse ligaklasse)
            throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();

        if (StringUtils.isEmpty(ligaklasse.getKlassenName())) {
            findings.add(
                    new ValidationMessage(MISSING, ligaklasse, "KlassenName"));
        }
        if (!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }

    public Ligaklasse findLigaklasseById(long ligaId) {
        return findById(Ligaklasse.class, ligaId);
    }

    public Ligaklasse findLigaklasseByName(String klassenName) {
        Query query = getSession()
                .createQuery(
                        "select k from Ligaklasse k where k.klassenName = ?");
        query.setCacheable(true);
        query.setString(0, klassenName);
        return (Ligaklasse) query.uniqueResult();
    }

    public List<Ligaklasse> findAllLigaklasse() {
        Query query = getSession()
                .createQuery(
                        "select l from Ligaklasse l order by l.rang");
        query.setCacheable(true);
        return query.list();
    }

    public void deleteLigaklasse(Ligaklasse aLiga) throws DartException {
        delete(aLiga);
    }
}
