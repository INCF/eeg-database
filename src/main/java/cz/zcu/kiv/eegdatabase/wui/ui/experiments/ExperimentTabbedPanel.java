/*******************************************************************************
 * This file is part of the EEG-database project
 * 
 *   ==========================================
 *  
 *   Copyright (C) 2013 by University of West Bohemia (http://www.zcu.cz/en/)
 *  
 *  ***********************************************************************************************************************
 *  
 *   Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *   the License. You may obtain a copy of the License at
 *  
 *       http://www.apache.org/licenses/LICENSE-2.0
 *  
 *   Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *   an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *   specific language governing permissions and limitations under the License.
 *  
 *  ***********************************************************************************************************************
 *  
 *   ExperimentTabbedPanel.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.wui.ui.experiments;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.extensions.markup.html.tabs.ITab;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.Loop;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Args;

import javax.xml.stream.events.Attribute;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Prasek
 * Date: 24.4.13
 * Time: 15:33
 * To change this template use File | Settings | File Templates.
 */
public class ExperimentTabbedPanel<T extends ITab> extends Panel{
  private static final long serialVersionUID = 1L;
  public static final String TAB_PANEL_ID = "panel";
  private final List<T> tabs;
  private int currentTab = -1;
  private transient Boolean[] tabsVisibilityCache;
  private WebMarkupContainer titleLink = null;
  private Component title = null;
  private Component environmentTitle = null;
  private LoopItem tab = null;
  private int[] colors = {0,0,0};

  public ExperimentTabbedPanel(String id, List<T> tabs)
  {
    this(id, tabs, null);
  }

  public ExperimentTabbedPanel(String id, List<T> tabs, IModel<Integer> model)
  {
    super(id, model);

    this.tabs = ((List)Args.notNull(tabs, "tabs"));

    IModel tabCount = new AbstractReadOnlyModel()
    {
      private static final long serialVersionUID = 1L;

      public Integer getObject()
      {
        return Integer.valueOf(ExperimentTabbedPanel.this.tabs.size());
      }
    };
    WebMarkupContainer tabsContainer = newTabsContainer("tabs-container");
    add(new Component[] { tabsContainer });

    tabsContainer.add(new Component[] { new Loop("tabs", tabCount)
    {
      private static final long serialVersionUID = 1L;

      protected void populateItem(LoopItem item)
      {
        tab = item;
        int index = item.getIndex();
        ITab tab = (ITab)ExperimentTabbedPanel.this.tabs.get(index);
        titleLink = ExperimentTabbedPanel.this.newLink("link", index);
        title = ExperimentTabbedPanel.this.newTitle("title", tab.getTitle(), index);

        if (colors[index] == 0) {
            title.add(AttributeModifier.replace("class", "whiteTab"));
        }
        else if (colors[index] == 1){
            title.add(AttributeModifier.replace("class", "greenTab"));
        }
        else if (colors[index] == 2){
            title.add(AttributeModifier.replace("class", "redTab"));
        }

        titleLink.add(new Component[] { title });
        item.add(new Component[] { titleLink });
      }

      protected LoopItem newItem(int iteration)
      {
        return ExperimentTabbedPanel.this.newTabContainer(iteration);
      }
    }
     });
    add(new Component[] { newPanel() });
  }

  protected IModel<?> initModel()
  {
    return new Model(Integer.valueOf(-1));
  }

  protected WebMarkupContainer newTabsContainer(String id)
  {
    return new WebMarkupContainer(id)
    {
      private static final long serialVersionUID = 1L;

      protected void onComponentTag(ComponentTag tag)
      {
        super.onComponentTag(tag);
        tag.put("class", ExperimentTabbedPanel.this.getTabContainerCssClass());
      }
    };
  }

  protected LoopItem newTabContainer(final int tabIndex)
  {
    return new LoopItem(tabIndex)
    {
      private static final long serialVersionUID = 1L;

      protected void onComponentTag(ComponentTag tag)
      {
        super.onComponentTag(tag);
        String cssClass = tag.getAttribute("class");
        if (cssClass == null)
        {
          cssClass = " ";
        }
        cssClass = cssClass + " tab" + getIndex();

        if (getIndex() == ExperimentTabbedPanel.this.getSelectedTab())
        {
          cssClass = cssClass + ' ' + ExperimentTabbedPanel.this.getSelectedTabCssClass();
        }
        if (getIndex() == ExperimentTabbedPanel.this.getTabs().size() - 1)
        {
          cssClass = cssClass + ' ' + ExperimentTabbedPanel.this.getLastTabCssClass();
        }
        tag.put("class", cssClass.trim());
      }

      public boolean isVisible()
      {
        return ((ITab)ExperimentTabbedPanel.this.getTabs().get(tabIndex)).isVisible();
      }
    };
  }

  protected void onBeforeRender()
  {
    int index = getSelectedTab();

    if ((index == -1) || (!isTabVisible(index)))
    {
      index = -1;
      for (int i = 0; i < this.tabs.size(); i++)
      {
        if (isTabVisible(i))
        {
          index = i;
          break;
        }
      }

      if (index != -1)
      {
        setSelectedTab(index);
      }
    }

    setCurrentTab(index);

    super.onBeforeRender();
  }

  protected String getTabContainerCssClass()
  {
    return "tab-row";
  }

  protected String getLastTabCssClass()
  {
    return "last";
  }

  protected String getSelectedTabCssClass()
  {
    return "selected";
  }

  public final List<T> getTabs()
  {
    return this.tabs;
  }

  protected Component newTitle(String titleId, IModel<?> titleModel, int index)
  {
    return new Label(titleId, titleModel);
  }

  protected WebMarkupContainer newLink(String linkId, final int index)
  {
    return new Link(linkId)
    {
      private static final long serialVersionUID = 1L;

      public void onClick()
      {
        ExperimentTabbedPanel.this.setSelectedTab(index);
      }
    };
  }

  public ExperimentTabbedPanel<T> setSelectedTab(int index)
  {
    if ((index < 0) || (index >= this.tabs.size()))
    {
      throw new IndexOutOfBoundsException();
    }

    setDefaultModelObject(Integer.valueOf(index));

    this.currentTab = -1;
    setCurrentTab(index);

    return this;
  }

  private void setCurrentTab(int index)
  {
    if (this.currentTab == index)
    {
      return;
    }
    this.currentTab = index;
    Component component;
    if ((this.currentTab == -1) || (this.tabs.size() == 0) || (!isTabVisible(this.currentTab)))
    {
      component = newPanel();
    }
    else
    {
      ITab tab = (ITab)this.tabs.get(this.currentTab);
      component = tab.getPanel("panel");
      if (component == null)
      {
        throw new WicketRuntimeException("ITab.getPanel() returned null. TabbedPanel [" + getPath() + "] ITab index [" + this.currentTab + "]");
      }

    }

    if (!component.getId().equals("panel"))
    {
      throw new WicketRuntimeException("ITab.getPanel() returned a panel with invalid id [" + component.getId() + "]. You must always return a panel with id equal to the provided panelId parameter. TabbedPanel [" + getPath() + "] ITab index [" + this.currentTab + "]");
    }

    addOrReplace(new Component[] { component });
  }

  private WebMarkupContainer newPanel()
  {
    return new WebMarkupContainer("panel");
  }

  public final int getSelectedTab()
  {
    return ((Integer)getDefaultModelObject()).intValue();
  }

  private boolean isTabVisible(int tabIndex)
  {
    if (this.tabsVisibilityCache == null)
    {
      this.tabsVisibilityCache = new Boolean[this.tabs.size()];
    }

    if (this.tabsVisibilityCache.length < tabIndex + 1)
    {
      Boolean[] resized = new Boolean[tabIndex + 1];
      System.arraycopy(this.tabsVisibilityCache, 0, resized, 0, this.tabsVisibilityCache.length);
      this.tabsVisibilityCache = resized;
    }

    if (this.tabsVisibilityCache.length > 0)
    {
      Boolean visible = this.tabsVisibilityCache[tabIndex];
      if (visible == null)
      {
        visible = Boolean.valueOf(((ITab)this.tabs.get(tabIndex)).isVisible());
        this.tabsVisibilityCache[tabIndex] = visible;
      }
      return visible.booleanValue();
    }

    return false;
  }

  protected void onDetach()
  {
    this.tabsVisibilityCache = null;
    super.onDetach();
  }

    public void setTabClassGreen(int index){
        colors[index] = 1;
    }

    public void setTabClassWhite(int index){
        colors[index] = 0;
    }

    public void setTabClassRed(int index){
        colors[index] = 2;
    }
}
