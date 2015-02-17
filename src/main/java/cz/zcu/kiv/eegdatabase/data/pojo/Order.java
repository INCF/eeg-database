package cz.zcu.kiv.eegdatabase.data.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="EEG_ORDER")
public class Order implements Serializable {
    
    private static final long serialVersionUID = -2384617279572198345L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ORDER_ID")
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "PERSON", nullable = false)
    private Person person;
    
    @Column(name = "DATE", nullable = false)
    private Timestamp date;
    
    @Column(name = "ORDER_PRICE", nullable = false)
    private BigDecimal orderPrice;
    
    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<OrderItem>();
    
    public Order() {

    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person person) {
        this.person = person;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }
    public BigDecimal getOrderPrice() {
        return orderPrice;
    }
    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }
    
    public List<OrderItem> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItem> items) {
        this.items = items;
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
        if (!(obj instanceof Order)) {
            return false;
        }
        Order other = (Order) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }
    
    
}
