package com.soosmart.facts.Implement;

import com.soosmart.facts.dto.proforma.ProformaDTO;
import com.soosmart.facts.dto.proforma.SaveProformaDTO;
import com.soosmart.facts.entity.Client;
import com.soosmart.facts.entity.Proforma;
import com.soosmart.facts.entity.Projet;
import com.soosmart.facts.mapper.ResponseMapper;
import com.soosmart.facts.repository.ClientDAO;
import com.soosmart.facts.repository.ProformaDAO;
import com.soosmart.facts.repository.ProjetDAO;
import com.soosmart.facts.service.ArticleQuantiteService;
import com.soosmart.facts.service.ProformaService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProformaImpl implements ProformaService {

    private final ProformaDAO proformaRepository;
    private final ProjetDAO projetRepository;
    private final ClientDAO clientRepository;
    private final ArticleQuantiteService articleQuantiteService;
    private final ResponseMapper responseMapper;


    @Override
    public ProformaDTO saveProforma(SaveProformaDTO saveProformaDTO) {
        Optional<Projet> projet = this.projetRepository.findById(saveProformaDTO.projet_id()).stream().findFirst();
        if (projet.isPresent()) {
            Proforma proforma = Proforma.builder()
                    .numero(this.GenerateproformaNumero())
                    .reference(saveProformaDTO.reference())
                    .projet(projet.get())
                    .client(projet.get().getClient())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                    .build();
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        }else {
            Optional<Client> client = this.clientRepository.findById(saveProformaDTO.client_id()).stream().findFirst();
            if (client.isEmpty()){
                throw new EntityExistsException("Client not found");
            }
            Proforma proforma = Proforma.builder()
                    .numero(this.GenerateproformaNumero())
                    .client(client.get())
                    .reference(saveProformaDTO.reference())
                    .articleQuantiteList(this.articleQuantiteService.saveAllArticleQuantitelist(saveProformaDTO.articleQuantiteslist()))
                    .build();
            return this.responseMapper.responseProformaDTO(this.proformaRepository.save(this.CalculateProformaTotal(proforma)));
        }
    }

    @Override
    public ProformaDTO updateProforma(SaveProformaDTO saveProformaDTO) {
        return null;
    }

    @Override
    public void deleteProforma(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if(proforma.isPresent()){
            try{
                this.proformaRepository.delete(proforma.get());
            }catch (Exception exception){
                System.out.println("Cause -> \n"+exception.getCause()+"\nmessage ->\n"+exception.getMessage());;
            }
        }
    }

    @Override
    public ProformaDTO getProforma(String numero) {
        Optional<Proforma> proforma = this.proformaRepository.findByNumero(numero);
        if (proforma.isPresent()){
            return this.responseMapper.responseProformaDTO(proforma.get());
        }
        else {
            throw new EntityExistsException("Donne non trouve avec ce numero de proforma");
        }
    }

    @Override
    public List<ProformaDTO> getProformas() {
        return this.proformaRepository.findAll().stream().map(this.responseMapper::responseProformaDTO).toList();
    }

    @Override
    public List<String> getProformasNumereList() {
        return this.proformaRepository.findAll().stream().map(Proforma::getNumero).toList();
    }

    public String GenerateproformaNumero(){
        // Obtenir l'année (2 derniers chiffres), le mois et le jour
        String year = String.valueOf(LocalDate.now().getYear()).substring(2); // Année (2 chiffres)
        String month = String.format("%02d", LocalDate.now().getMonthValue()); // Mois
        String day = String.format("%02d", LocalDate.now().getDayOfMonth());  // Jour
        Instant startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant(); // Début de la journée
        Instant endOfDay = LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant(); // Fin de la journée
        return "FP"+year+month+day+this.proformaRepository.countProformasCreateToday(startOfDay, endOfDay);
    }

    private Proforma CalculateProformaTotal(Proforma proforma){
        /*AtomicReference<Float> total_ht = new AtomicReference<>((float) 0);
        float total_tva;

        proforma.getArticleQuantiteList().forEach(
                articleQuantite -> {
                    proforma.getTotal_ht() += articleQuantite.getPrix_article() * articleQuantite.getQuantite().floatValue();

                });*/


        var objectStream = proforma.getArticleQuantiteList()
                .stream()
                .map(
                        articleQuantite ->
                                articleQuantite.getPrix_article() * articleQuantite.getQuantite()
                );
        proforma.setTotal_ht(
                objectStream.reduce(0f, Float::sum)
        );
        proforma.setTotal_tva(proforma.getTotal_ht()*0.18f);
        proforma.setTotal_ttc(proforma.getTotal_ht()+proforma.getTotal_tva());

        return proforma;
    }
}
