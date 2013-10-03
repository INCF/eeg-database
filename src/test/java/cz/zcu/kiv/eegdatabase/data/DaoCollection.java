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
 *   DaoCollection.java, 2013/10/02 00:01 Jakub Rinkes
 ******************************************************************************/
package cz.zcu.kiv.eegdatabase.data;

import cz.zcu.kiv.eegdatabase.data.dao.ArticleDao;
import cz.zcu.kiv.eegdatabase.data.dao.PersonDao;
import cz.zcu.kiv.eegdatabase.data.dao.ResearchGroupDao;
import cz.zcu.kiv.eegdatabase.data.dao.ReservationDao;

/**
 * Created: 5.5.11
 *
 * @author Jenda Kolena, jendakolena@gmail.com
 * @version 0.1
 */
public class DaoCollection
{
    private PersonDao personDao;
    private ResearchGroupDao researchGroupDao;
    private ReservationDao reservationDao;
    private ArticleDao articleDao;

    public DaoCollection(PersonDao personDao, ResearchGroupDao researchGroupDao, ReservationDao reservationDao, ArticleDao articleDao)
    {
        this.personDao = personDao;
        this.researchGroupDao = researchGroupDao;
        this.reservationDao = reservationDao;
        this.articleDao = articleDao;
    }

    public PersonDao getPersonDao()
    {
        return personDao;
    }

    public ResearchGroupDao getResearchGroupDao()
    {
        return researchGroupDao;
    }

    public ReservationDao getReservationDao()
    {
        return reservationDao;
    }

    public ArticleDao getArticleDao()
    {
        return articleDao;
    }
}
