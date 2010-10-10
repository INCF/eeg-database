/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.zcu.kiv.eegdatabase.data.persistent;

import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;

/**
 *
 * @author Petr Ježek
 */
public class ReverseEngStrategy extends DelegatingReverseEngineeringStrategy {

  public ReverseEngStrategy(ReverseEngineeringStrategy delegate) {
    super(delegate);
  }

  @Override
  public String getTableIdentifierStrategyName(TableIdentifier t) {
    return "increment";
  }
}
