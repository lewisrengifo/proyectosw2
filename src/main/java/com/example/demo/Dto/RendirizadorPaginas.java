package com.example.demo.Dto;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class RendirizadorPaginas<T> {
    private String url;
    private Page<T> page;
    private int totalPags;
    private int nElemPorPag;
    private int pagAcual;
    private List<ElementosPagina> paginas;

    public RendirizadorPaginas(String url,Page<T>page ){
        this.setUrl(url);
        this.setPage(page);
        this.setPaginas(new ArrayList<ElementosPagina>());

        setTotalPags(page.getTotalPages());
        setnElemPorPag(page.getSize());
        setPagAcual(page.getNumber()+1);

        int desde,hasta;
        desde=1;
        hasta= getTotalPags();
        for(int i=0;i<hasta;i++){
            getPaginas().add(new ElementosPagina(desde+1, getPagAcual() ==desde+1));
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTotalPags() {
        return totalPags;
    }

    public void setTotalPags(int totalPags) {
        this.totalPags = totalPags;
    }

    public int getPagAcual() {
        return pagAcual;
    }

    public void setPagAcual(int pagAcual) {
        this.pagAcual = pagAcual;
    }

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }

    public int getnElemPorPag() {
        return nElemPorPag;
    }

    public void setnElemPorPag(int nElemPorPag) {
        this.nElemPorPag = nElemPorPag;
    }

    public List<ElementosPagina> getPaginas() {
        return paginas;
    }

    public void setPaginas(List<ElementosPagina> paginas) {
        this.paginas = paginas;
    }
}
