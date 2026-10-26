
package com.logistics.fleet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DeliveryConfirmationRequest {

    @NotBlank
    @Size(max = 100)
    private String receiverName;

    @Size(max = 500)
    private String proofReference;

    @Size(max = 1000)
    private String notes;

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProofReference() {
        return proofReference;
    }

    public void setProofReference(String proofReference) {
        this.proofReference = proofReference;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
