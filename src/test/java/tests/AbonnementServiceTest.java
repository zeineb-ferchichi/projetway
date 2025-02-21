package tests;

import models.AbonnementTransportDTO;
import models.StatusAbonnement;
import models.TypeAbonnement;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AbonnementServiceTest {

    @Test
    public void testAbonnementValide() {
        AbonnementTransportDTO abonnement = new AbonnementTransportDTO(1, TypeAbonnement.MENSUEL, 50.0, 30, StatusAbonnement.ACTIF, 101, "Bus");

        assertNotNull(abonnement, "L'abonnement ne doit pas être null");
        assertEquals(TypeAbonnement.MENSUEL, abonnement.getTypeAbonnem(), "Le type d'abonnement est incorrect");
        assertEquals(StatusAbonnement.ACTIF, abonnement.getStatus(), "Le statut de l'abonnement est incorrect");
        assertTrue(abonnement.getDureeValable() > 0, "La durée valable doit être positive");
    }

    @Test
    public void testAbonnementAvecTransportInexistant() {
        AbonnementTransportDTO abonnement = new AbonnementTransportDTO(2, TypeAbonnement.ANNUEL, 500.0, 365, StatusAbonnement.ACTIF, 999, "Inconnu");

        assertNotEquals("Bus", abonnement.getTypeTransport(), "Le transport n'est pas valide");
        assertFalse(abonnement.getIdTransp() < 0, "L'ID du transport ne peut pas être négatif");
    }

    @Test
    public void testDureeValableInvalide() {
        AbonnementTransportDTO abonnement = new AbonnementTransportDTO(3, TypeAbonnement.HEBDOMADAIRE, 20.0, -5, StatusAbonnement.EXPIRE, 202, "Train");

        assertFalse(abonnement.getDureeValable() > 0, "La durée valable ne peut pas être négative");
    }
}
