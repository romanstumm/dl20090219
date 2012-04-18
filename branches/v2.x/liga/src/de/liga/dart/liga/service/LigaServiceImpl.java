package de.liga.dart.liga.service;

import de.liga.dart.common.service.AbstractService;
import de.liga.dart.exception.DartException;
import de.liga.dart.exception.DartValidationException;
import de.liga.dart.exception.ValidationMessage;
import static de.liga.dart.exception.ValidationReason.MISSING;
import de.liga.dart.model.Liga;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;

import java.util.ArrayList;
import java.util.List;

public class LigaServiceImpl extends AbstractService implements LigaService {

    public void saveLiga(Liga aLiga) {
        validate(aLiga);
        save(aLiga);
    }

    private void validate(Liga liga) throws DartValidationException {
        List<ValidationMessage> findings = new ArrayList<ValidationMessage>();

        if (StringUtils.isEmpty(liga.getLigaName())) {
            findings.add(new ValidationMessage(MISSING, liga, "LigaName"));
        }
        if(!findings.isEmpty()) {
            throw new DartValidationException(findings);
        }
    }
    
    public Liga findLigaById(long ligaId) {
        return findById(Liga.class, ligaId);
    }

    public Liga findLigaByName(String ligaName) {
        Query query = getSession()
                .createQuery("select l from Liga l where l.ligaName = ?");
        query.setCacheable(true);
        query.setString(0, ligaName);
        return (Liga) query.uniqueResult();
    }

    public List<Liga> findAllLiga() {
        Query query = getSession()
                .createQuery("select l from Liga l order by l.ligaName");
        query.setCacheable(true);
        return query.list();
    }

    public void deleteLiga(Liga aLiga) throws DartException {
        delete(aLiga);
    }
}
