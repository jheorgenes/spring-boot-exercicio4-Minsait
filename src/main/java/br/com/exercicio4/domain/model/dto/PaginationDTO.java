package br.com.exercicio4.domain.model.dto;

public class PaginationDTO {

    private int currentPage;
    private int totalPages;
    private int totalItems;

    public PaginationDTO(int currentPage, int totalPages, int totalItems) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
