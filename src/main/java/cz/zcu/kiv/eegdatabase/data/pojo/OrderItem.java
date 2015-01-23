package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EEG_ORDER_ITEM")
public class OrderItem implements Serializable, Comparable<OrderItem> {

    private static final long serialVersionUID = -7040903043604899895L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORDER_ITEM_ID")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @Column(name = "PRICE", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "EXPERIMENT")
    private Experiment experiment;
    
    @ManyToOne
    @JoinColumn(name = "EXPERIMENT_PACKAGE")
    private ExperimentPackage experimentPackage;
    
    @ManyToOne
    @JoinColumn(name = "LICENSE")
    private License license;

    public OrderItem() {

    }

    public OrderItem(Experiment experiment, Order order) {
        this.experiment = experiment;
        this.order = order;
        this.price = experiment.getPrice() != null ? experiment.getPrice() : BigDecimal.ZERO;
    }

    public OrderItem(ExperimentPackage experimentPackage, Order order) {
        this.experimentPackage = experimentPackage;
        this.order = order;
        this.price = experimentPackage.getPrice() != null ? experimentPackage.getPrice() : BigDecimal.ZERO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Experiment getExperiment() {
        return experiment;
    }

    public void setExperiment(Experiment experiment) {
        this.experiment = experiment;
    }

    public ExperimentPackage getExperimentPackage() {
        return experimentPackage;
    }

    public void setExperimentPackage(ExperimentPackage experimentPackage) {
        this.experimentPackage = experimentPackage;
    }
    
    public License getLicense() {
        return license;
    }
    
    public void setLicense(License license) {
        this.license = license;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof OrderItem)) {
            return false;
        }
        OrderItem other = (OrderItem) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OrderItem o) {

        if (o == null) {
            return 1;
        }

        return (id < o.getId()) ? -1 : ((id == o.getId()) ? 0 : 1);
    }

    public void setPriceFromItem() {
        
        if(experiment != null) {
            price = experiment.getPrice();
        } else {
            price = experimentPackage.getPrice();
        }
    }
}
