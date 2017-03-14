package tests;
//originally from : joins.C

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import ZIndex.ZIndexUtils;
import btree.BTFileScan;
import btree.BTreeFile;
import btree.KeyDataEntry;
import btree.LeafData;
import btree.StringKey;
import diskmgr.PCounter;
import edgeheap.Edge;
import global.AttrType;
import global.Descriptor;
import global.DescriptorRangePair;
import global.EID;
import global.NID;
import global.RID;
import global.SystemDefs;
import global.TupleOrder;
import heap.HFBufMgrException;
import heap.HFDiskMgrException;
import heap.HFException;
import heap.InvalidSlotNumberException;
import heap.Tuple;
import iterator.FileScan;
import iterator.FldSpec;
import iterator.RelSpec;
import iterator.Sort;
import nodeheap.NScan;
import nodeheap.Node;
import zbtree.DescriptorDataEntry;
import zbtree.DescriptorKey;
import zbtree.ZBTFileScan;
import zbtree.ZBTreeFile;

public class NodeQuery
{
    public final static boolean OK   = true; 
    public final static boolean FAIL = false;
    public void nodeQuery(String graphDBName,int numBuf,int qType,int index,String queryOptions) throws InvalidSlotNumberException, HFException, HFDiskMgrException, HFBufMgrException, Exception
    {

        switch(qType)
        {
        case 0: 

            System.out.println("  query will print the node data in the order it occurs in the node heap");
            if(index==0) {
                if(SystemDefs.JavabaseDB!=null) 
                    SystemDefs.JavabaseBM.flushAllPages();
                SystemDefs sysdef = new SystemDefs(graphDBName,0,numBuf,"Clock",0);
                SystemDefs.JavabaseBM.flushAllPages();

                // clean up
                try {
                    //iscan.close();
                    SystemDefs.JavabaseDB.btNodeLabel.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

//              
//              System.out.println("*******************************************************************\n\n\n");
//              
//              status = OK;
//              SystemDefs.JavabaseDB.ztNodeDesc = new ZBTreeFile(SystemDefs.JavabaseDBName+"_ZTreeNodeIndex", AttrType.attrString, 180, 1/*delete*/);
//              
//              // start index scan
//              ZBTFileScan izscan = null;
//              try {
//                  izscan = SystemDefs.JavabaseDB.ztNodeDesc.new_scan(null, null);
//              }
//              catch (Exception e) {
//                  status = FAIL;
//                  e.printStackTrace();
//              }
//
//              DescriptorDataEntry tz=null;
//              try {
//                  tz = izscan.get_next();
//              }
//              catch (Exception e) {
//                  status = FAIL;
//                  e.printStackTrace();
//              }
//              flag = true;
//              //System.out.println(t+""+iscan);
//              while (tz != null && izscan!=null) {
//          //System.out.println("hii");
//                  try {
//                      tz = izscan.get_next();
//                  }
//                  catch (Exception e) {
//                      status = FAIL;
//                      e.printStackTrace();
//                  }
//                  
//                  try {
//                      if(tz==null) break;
//                      DescriptorKey k = (DescriptorKey)tz.key;
//                      
//                      zbtree.LeafData l = (zbtree.LeafData)tz.data;
//                      NID nid =  l.getData();
//                      Node node = SystemDefs.JavabaseDB.nhfile.getNode(nid);
//                      System.out.println("Key: "+k.getKey()+" Label: "+node.getLabel()+" -- Descripotr: "+node.getDesc().value[0]+" "+node.getDesc().value[1]+" "+node.getDesc().value[2]+" "+node.getDesc().value[3]+" "+node.getDesc().value[4]);
//                  }
//                  catch (Exception e) {
//                      status = FAIL;
//                      e.printStackTrace();
//                  }
//              }
//
//              if (flag && status) {
//                  System.out.println("Test1 -- Index Scan OK");
//              }
//
//              // clean up
//              try {
//                  //iscan.close();
//                  SystemDefs.JavabaseDB.ztNodeDesc.close();
//              }
//              catch (Exception e) {
//                  status = FAIL;
//                  e.printStackTrace();
//              }
                
                
            }
            break;
        case 1:
            System.out.println("query will print the node data in increasing alphanumerical order of labels");
            if(index==0) {
                if(SystemDefs.JavabaseDB!=null) 
                    SystemDefs.JavabaseBM.flushAllPages();
                SystemDefs sysdef = new SystemDefs(graphDBName,0,numBuf,"Clock",0);
                SystemDefs.JavabaseBM.flushAllPages();

                boolean status = OK;

                AttrType[] attrType = new AttrType[2];
                attrType[0] = new AttrType(AttrType.attrDesc);
                attrType[1] = new AttrType(AttrType.attrString);
                short[] attrSize = new short[1];
                attrSize[0] = 20; //REC_LEN1;
                TupleOrder[] order = new TupleOrder[1];
                order[0] = new TupleOrder(TupleOrder.Ascending);
                //order[1] = new TupleOrder(TupleOrder.Descending);
                
                // create an iterator by open a file scan
                FldSpec[] projlist = new FldSpec[2];
                RelSpec rel = new RelSpec(RelSpec.outer); 
                projlist[0] = new FldSpec(rel, 1);
                projlist[1] = new FldSpec(rel, 2);

                FileScan fscan = null;

                try {
                    fscan = new FileScan(SystemDefs.JavabaseDBName+"_Node", attrType, attrSize, (short) 2, 2, projlist, null);
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }

                // Sort 
                Sort sort = null;
                try {
                    sort = new Sort(attrType, (short) 2, attrSize, fscan, 2, order[0], 20, 30);
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }


                int count = 0;
                Tuple t = null;
                String outval = null;

                try {
                    t = sort.get_next();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace(); 
                }

                boolean flag = true;

                while (t != null) {
                    
                    try 
                    {
                        //System.out.println("HI: "+t.getStrFld(1));
                        //System.out.println(t.getStrFld(2));
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }

                    try {
                        
                        Node node =new Node ();
                        node.nodeInit(t.getTupleByteArray(), 0);
                        System.out.print("Label:\t"+node.getLabel());
                        System.out.println("\tDescriptor: "+node.getDesc().getString());
                        t = sort.get_next();
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }
                }
                

                // clean up
                try {
                    //sort.close();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }

            } 
            else {

                if(SystemDefs.JavabaseDB!=null) 
                    SystemDefs.JavabaseBM.flushAllPages();
                SystemDefs sysdef = new SystemDefs(graphDBName,0,numBuf,"Clock",0);
                //SystemDefs.JavabaseBM.flushAllPages();

                boolean status = OK;
                SystemDefs.JavabaseDB.btNodeLabel = new BTreeFile(SystemDefs.JavabaseDBName+"_BTreeNodeIndex", AttrType.attrString, 32, 1/*delete*/);

                // start index scan
                BTFileScan iscan = null;
                try {
                    iscan = SystemDefs.JavabaseDB.btNodeLabel.new_scan(null, null);
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }

                KeyDataEntry t=null;
                try {
                    t = iscan.get_next();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }
                boolean flag = true;
                //System.out.println(t+""+iscan);
                while (t != null && iscan!=null) {
                    //System.out.println("hii");
                    try {
                        t = iscan.get_next();
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }

                    try {
                        if(t==null) break;
                        StringKey k = (StringKey)t.key;

                        LeafData l = (LeafData)t.data;
                        RID rid =  l.getData();
                        NID nid = new NID(rid.pageNo, rid.slotNo);
                        Node node = SystemDefs.JavabaseDB.nhfile.getNode(nid);
                        System.out.println("Label: "+node.getLabel()+" -- Descripotr: "+node.getDesc().value[0]+" "+node.getDesc().value[1]+" "+node.getDesc().value[2]+" "+node.getDesc().value[3]+" "+node.getDesc().value[4]);
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }
                }

                if (flag && status) {
                    System.out.println("Test1 -- Index Scan OK");
                }

                // clean up
                try {
                    //iscan.close();
                    SystemDefs.JavabaseDB.btNodeLabel.close();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }
            }

            break;

        case 2:
            System.out.println(" query will print the node data in increasing order of distance from a given " +
                    "5D target descriptor");
            Descriptor userDesc  = new Descriptor();
            String[] tokens = queryOptions.split(" ");
            userDesc.set(
                    Integer.parseInt(tokens[0]),
                    Integer.parseInt(tokens[1]),
                    Integer.parseInt(tokens[2]),
                    Integer.parseInt(tokens[3]),
                    Integer.parseInt(tokens[4])
            );
            int distance = Integer.parseInt(tokens[5]);
            if (index == 1) {
                boolean status = OK;
                SystemDefs.JavabaseDB.ztNodeDesc = new ZBTreeFile(SystemDefs.JavabaseDBName + "_ZTreeNodeIndex",
                        AttrType.attrString, 180, 1/*delete*/);

                // start index scan
                ZBTFileScan izscan = null;

            } else if (index == 0) {

            }
            break;
        case 3:
            System.out.println("then the query will take a target descriptor and a distance and return the labels of nodes with the given distance from the target descripto");
            userDesc  = new Descriptor();
            tokens = queryOptions.split(" ");
            userDesc.set(
                    Integer.parseInt(tokens[0]),
                    Integer.parseInt(tokens[1]),
                    Integer.parseInt(tokens[2]),
                    Integer.parseInt(tokens[3]),
                    Integer.parseInt(tokens[4])
            );

            distance = Integer.parseInt(tokens[5]);

            if (index == 1) {
                boolean status = OK;
                SystemDefs.JavabaseDB.ztNodeDesc = new ZBTreeFile(SystemDefs.JavabaseDBName+"_ZTreeNodeIndex",
                        AttrType.attrString, 180, 1/*delete*/);

                // start index scan
                ZBTFileScan izscan = null;
                Set<DescriptorRangePair> pairs = ZIndexUtils.getRangesForDescRange(
                        ZIndexUtils.getDiagonalDescFromDistance(new DescriptorKey(userDesc), distance));

                boolean flag = true;
                System.out.println(pairs.size());
                for (DescriptorRangePair pair: pairs) {
                    try {
                        System.out.println(pair.getStart().getKey());
                        izscan = SystemDefs.JavabaseDB.ztNodeDesc.new_scan(pair.getStart(), pair.getEnd());
                    } catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }

                    DescriptorDataEntry tz = null;
                    try {
                        tz = izscan.get_next();
                    } catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }
                    flag = true;
                    while (tz != null && izscan != null) {
                        try {
                            if (tz == null) break;
                            DescriptorKey k = (DescriptorKey) tz.key;

                            zbtree.LeafData l = (zbtree.LeafData) tz.data;
                            NID nid = l.getData();
                            Node node = SystemDefs.JavabaseDB.nhfile.getNode(nid);
                            System.out.println("Key: " + k.getKey() + " Label: " +
                                    node.getLabel() + " -- Descripotr: " + Arrays.toString(node.getDesc().value));
                        } catch (Exception e) {
                            status = FAIL;
                            e.printStackTrace();
                        }
                        
                        try {
                            tz = izscan.get_next();
                        } catch (Exception e) {
                            status = FAIL;
                            e.printStackTrace();
                        }


                    }
                }

                if (flag && status) {
                    System.out.println("Test3 -- OK");
                }

                // clean up
                try {
                    //iscan.close();
                    SystemDefs.JavabaseDB.ztNodeDesc.close();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }

            } else if (index == 0) {
                NScan scan = null;
                boolean status = OK;
                if ( status == OK ) {
                    System.out.println ("  - Take a target Desc and dist and return labels of nodes within distance\n");

                    try {
                        scan = SystemDefs.JavabaseDB.nhfile.openScan();
                    } catch (Exception e) {
                        status = FAIL;
                        System.err.println ("*** Error opening scan\n");
                        e.printStackTrace();
                    }

                    if ( status == OK &&  SystemDefs.JavabaseBM.getNumUnpinnedBuffers()
                            == SystemDefs.JavabaseBM.getNumBuffers() ) {
                        System.err.println ("*** The heap-file scan has not pinned the first page\n");
                        status = FAIL;
                    }
                }
                NID nidTmp = new NID();

                if ( status == OK ) {
                    Node node = null;

                    boolean done = false;
                    while (!done) {
                        node = scan.getNext(nidTmp);
                        if (node == null) {
                            done = true;
                            //break;
                        }

                        if (userDesc.distance(node.getDesc())<distance) {
                            System.out.print("Label: " + node.getLabel());
                            System.out.println(" Descriptor: " + node.getDesc().getString());
                        }
                    }

                }
                scan.closescan();
            }
            break;
        case 4:
            System.out.println("query will take a label and return all relevant information (including outgoing and incoming edges) about the node with the matching labe");
            List<EID> listEid = new ArrayList<EID>();
            Node matchNode = null;
            if(index==0) {
                if(SystemDefs.JavabaseDB!=null) 
                    SystemDefs.JavabaseBM.flushAllPages();
                SystemDefs sysdef = new SystemDefs(graphDBName,0,numBuf,"Clock",0);
                SystemDefs.JavabaseBM.flushAllPages();
                
                 NID nid =SystemDefs.JavabaseDB.nhfile.getNID(queryOptions);
                if(nid!=null)   
                  { listEid= SystemDefs.JavabaseDB.ehfile.getEIDList(nid);
                  matchNode = SystemDefs.JavabaseDB.nhfile.getNode(nid);
                  }
            } 
            else {
                if(SystemDefs.JavabaseDB!=null) 
                    SystemDefs.JavabaseBM.flushAllPages();
                SystemDefs sysdef = new SystemDefs(graphDBName,0,numBuf,"Clock",0);
                //SystemDefs.JavabaseBM.flushAllPages();
                
                boolean status = OK;
                SystemDefs.JavabaseDB.btNodeLabel = new BTreeFile(SystemDefs.JavabaseDBName+"_BTreeNodeIndex", AttrType.attrString, 32, 1/*delete*/);
                
                // start index scan
                BTFileScan iscan = null;
                try {
                    iscan = SystemDefs.JavabaseDB.btNodeLabel.new_scan(null, null);
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }

                KeyDataEntry t=null;
                try {
                    t = iscan.get_next();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }
                boolean flag = true;
                //System.out.println(t+""+iscan);
                while (t != null && iscan!=null) {
            //System.out.println("hii");
                    try {
                        t = iscan.get_next();
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }
                    
                    try {
                        if(t==null) break;
                        StringKey k = (StringKey)t.key;
                        
                        LeafData l = (LeafData)t.data;
                        RID rid =  l.getData();
                        NID nid = new NID(rid.pageNo, rid.slotNo);
                        Node node = SystemDefs.JavabaseDB.nhfile.getNode(nid);
                        if(node.getLabel().equalsIgnoreCase(queryOptions))
                        {
                            listEid= SystemDefs.JavabaseDB.ehfile.getEIDList(nid);
                            matchNode = node;
                            break;                          
                        }
                    
                    }
                    catch (Exception e) {
                        status = FAIL;
                        e.printStackTrace();
                    }
                }

                // clean up
                try {
                    //iscan.close();
                    SystemDefs.JavabaseDB.btNodeLabel.close();
                }
                catch (Exception e) {
                    status = FAIL;
                    e.printStackTrace();
                }
            }
                if(matchNode!=null&&!listEid.isEmpty())
                {
                    System.out.println("Match Found: ");
                    System.out.println("Node Label:"+ matchNode.getLabel());
                    System.out.println("Node Descriptor: "+ matchNode.getDesc().value[0]+" "+matchNode.getDesc().value[1]+" "+ matchNode.getDesc().value[2]+" "+matchNode.getDesc().value[3]+" "+matchNode.getDesc().value[4]);
                        
                    List<Edge> listSource= new ArrayList<Edge>();
                    List<Edge> listDes= new ArrayList<Edge>();
                    for(EID i:listEid)
                    {
                        Edge curEdge = SystemDefs.JavabaseDB.ehfile.getEdge(i);
                        NID nidSource =curEdge.getSource();
                        NID nidDes = curEdge.getDestination();
                        String nodeSource =  SystemDefs.JavabaseDB.nhfile.getNode(nidSource).getLabel();
                        String nodeDes =  SystemDefs.JavabaseDB.nhfile.getNode(nidDes).getLabel();
                        if(nodeSource.equalsIgnoreCase(queryOptions))   listSource.add(curEdge);
                        if(nodeDes.equalsIgnoreCase(queryOptions))   listDes.add(curEdge);
                    }
                    if(!listDes.isEmpty())
                    {
                        System.out.println("Node Incomming Edges: "+listDes.size());
                                        for(Edge e:listDes)
                    {
                         String src= SystemDefs.JavabaseDB.nhfile.getNode(e.getSource()).getLabel();
                        System.out.println("Source Node: "+src+" Label: "+e.getLabel() + " Weight: "+e.getWeight());
                    }
                    }
                    else
                    System.out.println("Node Incomming Edges: "+listDes.size());
                    if(!listSource.isEmpty())
                    {
                        System.out.println("Node Outgoing Edges: "+listSource.size());
                    for(Edge e:listSource)
                    {
                         String des= SystemDefs.JavabaseDB.nhfile.getNode(e.getDestination()).getLabel();
                        System.out.println("Destination Node: "+des+" Label: "+e.getLabel() + " Weight: "+e.getWeight());
                    }
                    }
                    else    System.out.println("Node Outgoing Edges: "+listSource.size());
                    
                }
                else 
                    System.out.println("No mactch found!!");
            
            break;
        case 5:
            System.out.println("query will take a target descriptor and a distance and return all relevant information (including outgoing and incoming edges) about the nodes with the given distance from the target descriptor.");
            break;

        default:
            System.out.println("invalid option");
            break;
        }

        System.out.println("");
        System.out.println("Node Count: "+SystemDefs.JavabaseDB.getNodeCnt());

        System.out.println("Edge Count: "+SystemDefs.JavabaseDB.getEdgeCnt());

        System.out.println("Disk Read Count: "+PCounter.readCounter);

        System.out.println("Disk Write Count: "+PCounter.writeCounter);

    }
    /*public static void main(String[] args) throws InvalidTupleSizeException, IOException, HashOperationException, PageUnpinnedException, PagePinnedException, PageNotFoundException, BufMgrException
    {
        String graphDBName=args[0];
        int numBuf=Integer.parseInt(args[1]);
        int qType=Integer.parseInt(args[2]);
        int index=Integer.parseInt(args[3]);
        int queryOptions=Integer.parseInt(args[4]);
        //NodeQuery nodequery=new NodeQuery(graphDBName, numBuf,qType,index,queryOptions);
    }*/
}   
