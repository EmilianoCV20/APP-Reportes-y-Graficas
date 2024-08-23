/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
//
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
//
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 *
 * @author emili
 */
public class Reporte extends javax.swing.JFrame implements Printable {

    /**
     * Creates new form Reporte
     */
    public Reporte() {
        initComponents();
        Connection con = ConexionSQL.getConexion();
        CargarTablaReporte();
        if(con != null){

        this.jLabelL.setIcon(new XYLineChart(this.jPanel1.getSize()));
        this.jLabelL.setText("");

        this.jLabel2.setIcon(new AreaChart(this.jPanel2.getSize()));
        this.jLabel2.setText("");

        this.jLabel4.setIcon(new AChart(this.jPanel3.getSize()));
        this.jLabel4.setText("");
        }
    }

    private void CargarTablaReporte() {
        DefaultTableModel modeloTabla = (DefaultTableModel) jTableReporte.getModel();
        modeloTabla.setRowCount(0);

        PreparedStatement ps;
        ResultSet rs;
        ResultSetMetaData rsmd;
        int columnas;

        try {
            Connection con = ConexionSQL.getConexion();
            ps = con.prepareStatement("SELECT Resort.NombreR, Cliente.NumPersonas, Reservacion.MesHospedaje FROM Reservacion\n"
                    + "JOIN Resort ON Resort.ID_Resort = Reservacion.ID_Resort\n"
                    + "JOIN Cliente ON Cliente.ID_Cliente = Reservacion.ID_Cliente\n"
                    + "ORDER BY Resort.NumEstrellas");

            rs = ps.executeQuery();
            rsmd = rs.getMetaData();
            columnas = rsmd.getColumnCount();

            while (rs.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = rs.getObject(i + 1);
                }
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
        }
    }

    public class XYLineChart extends ImageIcon {

        public XYLineChart(Dimension d) {
            XYDataset xydataset = xyDataset();
            JFreeChart jfreechart = ChartFactory.createXYLineChart(
                    "RESERVACIONES DE ESTE AÑO", "Meses del año", "Numero de Huespedes",
                    xydataset, PlotOrientation.VERTICAL, true, true, false);

            XYPlot xyplot = (XYPlot) jfreechart.getPlot();
            xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
            xyplot.setDomainGridlinePaint(Color.BLACK);
            xyplot.setRangeGridlinePaint(Color.BLACK);
            XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
            xylineandshaperenderer.setBaseShapesVisible(true);
            //Muestra los valores de cada punto XY
            XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
            xylineandshaperenderer.setBaseItemLabelGenerator(xy);
            xylineandshaperenderer.setBaseItemLabelsVisible(true);
            xylineandshaperenderer.setBaseLinesVisible(true);
            xylineandshaperenderer.setBaseItemLabelsVisible(true);

            BufferedImage bufferedImage = jfreechart.createBufferedImage(d.width, d.height);
            this.setImage(bufferedImage);
        }

        private XYDataset xyDataset() {
            XYSeries Resort1 = new XYSeries("Magnificent Resort");
            XYSeries Resort2 = new XYSeries("Grandstar Resort");
            XYSeries Resort3 = new XYSeries("Kurumba Resort");
            //serie #1
            Resort1.add(1, 10);
            Resort1.add(2, 0);
            Resort1.add(3, 15);
            Resort1.add(4, 12);
            Resort1.add(5, 0);
            Resort1.add(6, 0);
            Resort1.add(7, 0);
            Resort1.add(8, 0);
            Resort1.add(9, 3);
            Resort1.add(10, 0);
            Resort1.add(11, 0);
            Resort1.add(12, 5);
            //serie #2
            Resort2.add(1, 0);
            Resort2.add(2, 0);
            Resort2.add(3, 15);
            Resort2.add(4, 18);
            Resort2.add(5, 0);
            Resort2.add(6, 4);
            Resort2.add(7, 0);
            Resort2.add(8, 0);
            Resort2.add(9, 0);
            Resort2.add(10, 0);
            Resort2.add(11, 17);
            Resort2.add(12, 10);
            //serie #3
            Resort3.add(1, 18);
            Resort3.add(2, 14);
            Resort3.add(3, 0);
            Resort3.add(4, 0);
            Resort3.add(5, 0);
            Resort3.add(6, 0);
            Resort3.add(7, 9);
            Resort3.add(8, 17);
            Resort3.add(9, 0);
            Resort3.add(10, 4);
            Resort3.add(11, 0);
            Resort3.add(12, 0);

            XYSeriesCollection xyseriescollection = new XYSeriesCollection();
            xyseriescollection.addSeries(Resort1);
            xyseriescollection.addSeries(Resort2);
            xyseriescollection.addSeries(Resort3);

            return xyseriescollection;
        }
    }

    public class AreaChart extends ImageIcon {

        public AreaChart(Dimension d) {
            //Datos
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            dataset.setValue(18, "Kurumba Resort", "Ene");
            dataset.setValue(14, "Kurumba Resort", "Feb");
            dataset.setValue(0, "Kurumba Resort", "Mar");
            dataset.setValue(0, "Kurumba Resort", "Abr");
            dataset.setValue(0, "Kurumba Resort", "May");
            dataset.setValue(0, "Kurumba Resort", "Jun");
            dataset.setValue(9, "Kurumba Resort", "Jul");
            dataset.setValue(17, "Kurumba Resort", "Ago");
            dataset.setValue(0, "Kurumba Resort", "Sep");
            dataset.setValue(4, "Kurumba Resort", "Oct");
            dataset.setValue(0, "Kurumba Resort", "Nov");
            dataset.setValue(0, "Kurumba Resort", "Dic");

            dataset.setValue(10, "Magnificent Resort", "Ene");
            dataset.setValue(0, "Magnificent Resort", "Feb");
            dataset.setValue(15, "Magnificent Resort", "Mar");
            dataset.setValue(12, "Magnificent Resort", "Abr");
            dataset.setValue(0, "Magnificent Resort", "May");
            dataset.setValue(0, "Magnificent Resort", "Jun");
            dataset.setValue(0, "Magnificent Resort", "Jul");
            dataset.setValue(0, "Magnificent Resort", "Ago");
            dataset.setValue(3, "Magnificent Resort", "Sep");
            dataset.setValue(0, "Magnificent Resort", "Oct");
            dataset.setValue(0, "Magnificent Resort", "Nov");
            dataset.setValue(5, "Magnificent Resort", "Dic");

            dataset.setValue(0, "Grandstar Resort", "Ene");
            dataset.setValue(0, "Grandstar Resort", "Feb");
            dataset.setValue(15, "Grandstar Resort", "Mar");
            dataset.setValue(18, "Grandstar Resort", "Abr");
            dataset.setValue(0, "Grandstar Resort", "May");
            dataset.setValue(4, "Grandstar Resort", "Jun");
            dataset.setValue(0, "Grandstar Resort", "Jul");
            dataset.setValue(0, "Grandstar Resort", "Ago");
            dataset.setValue(0, "Grandstar Resort", "Sep");
            dataset.setValue(0, "Grandstar Resort", "Oct");
            dataset.setValue(17, "Grandstar Resort", "Nov");
            dataset.setValue(10, "Grandstar Resort", "Dic");

            //Grafico
            JFreeChart jfreechart = ChartFactory.createAreaChart(
                    "RESERVACIONES DE ESTE AÑO", "Meses del año", "Numero de Huespedes",
                    dataset, PlotOrientation.HORIZONTAL, true, true, false);

            jfreechart.setBackgroundPaint(Color.lightGray);
            jfreechart.getTitle().setPaint(Color.black);
            CategoryPlot p = jfreechart.getCategoryPlot();
            p.setRangeGridlinePaint(Color.red);

            BufferedImage bufferedImage = jfreechart.createBufferedImage(d.width, d.height);
            this.setImage(bufferedImage);
        }
    }

    public class AChart extends ImageIcon {

        public AChart(Dimension d) {
            XYDataset dataset = createDataset();
            JFreeChart jfreechart = ChartFactory.createScatterPlot(
                    "RESERVACIONES DE ESTE AÑO", "Meses del año", "Numero de Huespedes", dataset);

            XYPlot plot = (XYPlot) jfreechart.getPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.white);
            jfreechart.getTitle().setPaint(Color.BLACK);
            BufferedImage bufferedImage = jfreechart.createBufferedImage(d.width, d.height);
            this.setImage(bufferedImage);
        }

        private XYDataset createDataset() {
            XYSeries Resort1 = new XYSeries("Magnificent Resort");
            XYSeries Resort2 = new XYSeries("Grandstar Resort");
            XYSeries Resort3 = new XYSeries("Kurumba Resort");
            //serie #1
            Resort1.add(1, 10);
            Resort1.add(2, 0);
            Resort1.add(3, 15);
            Resort1.add(4, 12);
            Resort1.add(5, 0);
            Resort1.add(6, 0);
            Resort1.add(7, 0);
            Resort1.add(8, 0);
            Resort1.add(9, 3);
            Resort1.add(10, 0);
            Resort1.add(11, 0);
            Resort1.add(12, 5);
            //serie #2
            Resort2.add(1, 0);
            Resort2.add(2, 0);
            Resort2.add(3, 15);
            Resort2.add(4, 18);
            Resort2.add(5, 0);
            Resort2.add(6, 4);
            Resort2.add(7, 0);
            Resort2.add(8, 0);
            Resort2.add(9, 0);
            Resort2.add(10, 0);
            Resort2.add(11, 17);
            Resort2.add(12, 10);
            //serie #3
            Resort3.add(1, 18);
            Resort3.add(2, 14);
            Resort3.add(3, 0);
            Resort3.add(4, 0);
            Resort3.add(5, 0);
            Resort3.add(6, 0);
            Resort3.add(7, 9);
            Resort3.add(8, 17);
            Resort3.add(9, 0);
            Resort3.add(10, 4);
            Resort3.add(11, 0);
            Resort3.add(12, 0);

            XYSeriesCollection xyseriescollection = new XYSeriesCollection();
            xyseriescollection.addSeries(Resort1);
            xyseriescollection.addSeries(Resort2);
            xyseriescollection.addSeries(Resort3);

            return xyseriescollection;
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        jButtonPDF.setVisible(false);
        jButtonVolver.setVisible(false);
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.scale(pageFormat.getImageableWidth() / this.getWidth(), pageFormat.getImageableHeight() / this.getHeight());
        this.printAll(g2d);
        jButtonPDF.setVisible(true);
        jButtonVolver.setVisible(true);
        
        return PAGE_EXISTS;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTitulo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableReporte = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelL = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jButtonPDF = new javax.swing.JButton();
        jButtonVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("REPORTE DE RESERVACIONES");

        jPanelTitulo.setBackground(new java.awt.Color(102, 0, 0));
        jPanelTitulo.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED, java.awt.Color.red, java.awt.Color.red, java.awt.Color.red, java.awt.Color.red));

        jLabel1.setBackground(new java.awt.Color(255, 0, 0));
        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Reporte de Reservaciones en Resorts");
        jLabel1.setOpaque(true);

        javax.swing.GroupLayout jPanelTituloLayout = new javax.swing.GroupLayout(jPanelTitulo);
        jPanelTitulo.setLayout(jPanelTituloLayout);
        jPanelTituloLayout.setHorizontalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addGap(330, 330, 330)
                .addComponent(jLabel1)
                .addContainerGap(314, Short.MAX_VALUE))
        );
        jPanelTituloLayout.setVerticalGroup(
            jPanelTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTableReporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTableReporte.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre Resort", "Numero Personas", "Mes Hospedaje"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableReporte);

        jLabel3.setBackground(new java.awt.Color(153, 153, 153));
        jLabel3.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("REPORTE DE ESTANCIAS");
        jLabel3.setOpaque(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelL.setText("TABLA");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelL)
                .addGap(0, 468, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabelL)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setText("TABLA");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(0, 264, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setText("TABLA");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButtonPDF.setText("GUARDAR REPORTE");
        jButtonPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPDFActionPerformed(evt);
            }
        });

        jButtonVolver.setText("VOLVER A LOS DATOS");
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jLabel3)
                        .addGap(27, 27, 27)
                        .addComponent(jButtonVolver)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonPDF)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jButtonPDF)
                    .addComponent(jButtonVolver))
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVolverActionPerformed
        this.dispose();
        Tablas ventana = new Tablas();
        ventana.setVisible(true);
    }//GEN-LAST:event_jButtonVolverActionPerformed

    private void jButtonPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPDFActionPerformed
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);
        boolean doPrint = job.printDialog(); // mostrar diálogo de impresión
        if (doPrint) {
            try {
                job.setJobName("REPORTE");
                job.print();
            } catch (PrinterException e) {
                // aquí maneja los errores al imprimir
            }
        }

    }//GEN-LAST:event_jButtonPDFActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Reporte.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Reporte.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Reporte.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Reporte.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Reporte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonPDF;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelL;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelTitulo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableReporte;
    // End of variables declaration//GEN-END:variables
}
