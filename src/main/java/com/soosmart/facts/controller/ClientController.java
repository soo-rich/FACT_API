package com.soosmart.facts.controller;

import com.soosmart.facts.dto.client.ClientDTO;
import com.soosmart.facts.dto.client.SaveClientDTO;
import com.soosmart.facts.service.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(value = "client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping()
    public ResponseEntity<List<ClientDTO>> listResponseEntity(){
        return ResponseEntity.status(HttpStatus.OK).body(this.clientService.list());
    }

    @PostMapping()
    public ResponseEntity<ClientDTO> save(@RequestBody SaveClientDTO saveClientDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(this.clientService.save(saveClientDTO));
    }

    @PutMapping(path = "{id}" )
    public ResponseEntity<ClientDTO> update(@PathVariable UUID id, @RequestBody SaveClientDTO saveClientDTO){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.update(id, saveClientDTO));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Boolean> changePotiential(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(this.clientService.changePotential(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(this.clientService.delete(id));
    }

}
