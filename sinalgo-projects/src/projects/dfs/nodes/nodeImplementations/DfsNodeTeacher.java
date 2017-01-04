// package projects.dfs.nodes.nodeImplementations;
//
// import java.awt.Color;
// import java.awt.Graphics;
// import java.util.Iterator;
//
// import projects.dfs.nodes.messages.tokenMessage;
// import projects.dfs.nodes.timers.initTimer;
// import sinalgo.configuration.WrongConfigurationException;
// import sinalgo.gui.transformation.PositionTransformation;
// import sinalgo.nodes.Node;
// import sinalgo.nodes.edges.Edge;
// import sinalgo.nodes.messages.Inbox;
// import sinalgo.nodes.messages.Message;
// import sinalgo.tools.Tools;
//
//
//
// public class DfsNode extends Node {
//
//
// public int pere;
// public boolean visite[];
// public Color couleur=Color.blue;
//
// public void preStep() {}
//
// // ATTENTION lorsque init est appelŽ les liens de communications n'existent
// pas
// // il faut attend une unitŽ de temps, avant que les connections soient
// rŽalisŽes
// // nous utilisons donc un timer
//
// public void init() {
// // TODO Auto-generated method stub
// (new initTimer()).startRelative(1, this);
//
// }
//
//
// public int getIndex(Node n)
// {
// Iterator<Edge> iter=this.outgoingConnections.iterator();
// int j=1;
//
// while(true){
// if(iter.next().endNode.ID==n.ID) return j;
// j++;
// }
// }
//
// public Node getVoisin(int i){
// Iterator<Edge> iter=this.outgoingConnections.iterator();
//
// for(int j=1;j<i;j++)
// iter.next();
//
// return iter.next().endNode;
// }
//
// public int nbVoisin()
// {
// return this.outgoingConnections.size();
// }
//
//
// // Lorsque le timer prŽcŽdent expire, la fonction start est appelŽe
// // elle correspond ainsi ˆ l'initialisation rŽelle du processus
//
// public void start(){
//
// this.visite=new boolean[this.nbVoisin()+1];
// for(int i=0;i<=this.nbVoisin();i++) this.visite[i]=false;
//
//
// if(this.ID==1) {
// this.visite[1]=true;
// this.pere=-1;
// this.send(new tokenMessage(this),this.getVoisin(1));
// this.inverse();
// }
// else {
// this.pere=0;
// }
// System.out.print("\n"+this.ID+" : ");
// for(int i=1;i<=nbVoisin();i++)
// System.out.print(this.getVoisin(i).ID+","+this.getIndex(this.getVoisin(i))+";
// ");
//
// }
//
// // Cette fonction gre la rŽception de message
// // Elle est appelŽe rŽgulirement mm si aucun message n'a ŽtŽ reu
//
// public void handleMessages(Inbox inbox) {
// // Test si il y a des messages
// while(inbox.hasNext())
// {
// Message m=inbox.next();
// if(m instanceof tokenMessage) // Si le processus a reu le jeton
// {
// tokenMessage msg = (tokenMessage) m;
//
//
// if(this.pere==0){
// this.pere=this.getIndex((msg).sender);
// this.inverse();
// }
//
// boolean fini=true;
// for(int i=1;i<=this.nbVoisin();i++) {fini = fini && this.visite[i];}
//
// if(fini) {
//
// // modif
// //Tools.stopSimulation();
//
// for(int i=0;i<=this.nbVoisin();i++) this.visite[i]=false;
// this.visite[1]=true;
// this.pere=-1;
// this.send(new tokenMessage(this),this.getVoisin(1));
// this.inverse();
// }
// else
// {
// int suivant=0;
// if(this.pere!=this.getIndex((msg).sender) &&
// !this.visite[this.getIndex((msg).sender)]){
// suivant=this.getIndex((msg).sender);
// }
// else
// {
// for(int i=1;i<=this.nbVoisin();i++) {
// if(this.pere !=i && !this.visite[i])
// {
// suivant=i;
// break;
// }
// }
// if(suivant == 0){
// suivant=this.pere;
//
// }
// }
// this.visite[suivant]=true;
// this.send(new tokenMessage(this),this.getVoisin(suivant));
// // modif
// if(suivant==this.pere){
// // modif
// for(int i=0;i<=this.nbVoisin();i++) this.visite[i]=false;
// this.pere=0;
//
// }
// }
// }
// }
// }
//
//
// public void inverse(){
// if(this.couleur==Color.blue) this.couleur=Color.red;
// else this.couleur=Color.blue;
// }
//
// // La donction ci-dessous
// // calcule le voisin de droite
// // c'est-ˆ-dire le voisin qui n'est pas celui de gauche !
//
// public void neighborhoodChange() {}
// public void postStep() {}
// public void checkRequirements() throws WrongConfigurationException {}
//
//
//
// // affichage du noeud
//
// public void draw(Graphics g, PositionTransformation pt, boolean highlight){
// this.setColor(this.couleur);
// String text = ""+this.ID;
// super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.black);
// }
//
//
// }
