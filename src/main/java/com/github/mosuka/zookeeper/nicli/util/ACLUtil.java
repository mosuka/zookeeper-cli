/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mosuka.zookeeper.nicli.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

public class ACLUtil {
    public static List<Map<String, Object>> acl2List(List<ACL> acl) {
        List<Map<String, Object>> aclMapList = new ArrayList<Map<String, Object>>();

        for (ACL a : acl) {
            Map<String, Object> aclMap = new HashMap<String, Object>();
            aclMap.put("id", a.getId());
            aclMap.put("permissions", getPermString(a.getPerms()));
            aclMapList.add(aclMap);
        }

        return aclMapList;
    }

    public static int getPermFromString(String permString) {
        int perm = 0;
        for (int i = 0; i < permString.length(); i++) {
            switch (permString.charAt(i)) {
            case 'r':
                perm |= ZooDefs.Perms.READ;
                break;
            case 'w':
                perm |= ZooDefs.Perms.WRITE;
                break;
            case 'c':
                perm |= ZooDefs.Perms.CREATE;
                break;
            case 'd':
                perm |= ZooDefs.Perms.DELETE;
                break;
            case 'a':
                perm |= ZooDefs.Perms.ADMIN;
                break;
            default:
                System.err.println("Unknown perm type: " + permString.charAt(i));
            }
        }
        return perm;
    }

    public static String getPermString(int perms) {
        StringBuilder p = new StringBuilder();
        if ((perms & ZooDefs.Perms.CREATE) != 0) {
            p.append('c');
        }
        if ((perms & ZooDefs.Perms.DELETE) != 0) {
            p.append('d');
        }
        if ((perms & ZooDefs.Perms.READ) != 0) {
            p.append('r');
        }
        if ((perms & ZooDefs.Perms.WRITE) != 0) {
            p.append('w');
        }
        if ((perms & ZooDefs.Perms.ADMIN) != 0) {
            p.append('a');
        }
        return p.toString();
    }

    public static List<ACL> parseACLs(String aclString) {
        List<ACL> acl;
        String acls[] = aclString.split(",");
        acl = new ArrayList<ACL>();
        for (String a : acls) {
            int firstColon = a.indexOf(':');
            int lastColon = a.lastIndexOf(':');
            if (firstColon == -1 || lastColon == -1 || firstColon == lastColon) {
                System.err.println(a + " does not have the form scheme:id:perm");
                continue;
            }
            ACL newAcl = new ACL();
            newAcl.setId(new Id(a.substring(0, firstColon), a.substring(firstColon + 1, lastColon)));
            newAcl.setPerms(getPermFromString(a.substring(lastColon + 1)));
            acl.add(newAcl);
        }
        return acl;
    }
}
