// -*- coding: UTF-8 -*-
package projetoGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


@SuppressWarnings("serial")
public class GamePanel extends JPanel {
    private JTextField respostaField;
    private JButton verificarButton;
    private JLabel operacaoLabel;
    private JLabel pontosLabel;
    private JButton githubButton;
    private GameLogic gameLogic;

    public GamePanel() {
        gameLogic = new GameLogic();

        setLayout(new GridLayout(5, 1));

        operacaoLabel = new JLabel();
        operacaoLabel.setHorizontalAlignment(JLabel.CENTER);
        add(operacaoLabel);

        respostaField = new JTextField(10);
        respostaField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // Não é necessário implementar keyTyped para este caso
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processarResposta();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Não é necessário implementar keyReleased para este caso
            }
        });
        add(respostaField);

        verificarButton = new JButton("Verificar");
        verificarButton.addActionListener(new VerificarListener());
        verificarButton.setForeground(Color.BLUE);
        add(verificarButton);

        pontosLabel = new JLabel("Pontos: 0");
        pontosLabel.setHorizontalAlignment(JLabel.CENTER);
        pontosLabel.setOpaque(true); // Define o componente como opaco
        pontosLabel.setBackground(Color.yellow);
        add(pontosLabel);
        
        githubButton = new JButton("Visite o meu GitHub");
        githubButton.addActionListener(new GithubListener());
        githubButton.setPreferredSize(new Dimension(150, 10)); // Ajuste a largura e altura conforme necessário
        githubButton.setBackground(Color.lightGray); // Defina a cor desejada
        githubButton.setForeground(Color.BLACK); // Define a cor do texto para garantir visibilidade
        add(githubButton, BorderLayout.SOUTH);
        
        mostrarNovaOperacao();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 250);
    }

    private void mostrarNovaOperacao() {
        gameLogic.gerarNovaOperacao();
        operacaoLabel.setText(gameLogic.getOperacaoTexto());
        respostaField.setText("");
    }

    private void processarResposta() {
        try {
            int resposta = Integer.parseInt(respostaField.getText());
            if (gameLogic.verificarResposta(resposta)) {
                gameLogic.incrementarPontos();
                pontosLabel.setText("Pontos: " + gameLogic.getPontos());
                playAcertoSound();
            } else {
                playErroSound();
            }
            mostrarNovaOperacao();
        } catch (NumberFormatException ex) {
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    playInvalidNumberSound();
                    return null;
                }

                @Override
                protected void done() {
                    JOptionPane.showMessageDialog(GamePanel.this, "Digite um número válido!");
                }
            }.execute();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void playAcertoSound() {
        playSound("projetoGame/success.wav");
    }

    private void playErroSound() {
        playSound("projetoGame/error.wav");
    }

    private void playInvalidNumberSound() {
        playSound("projetoGame/notificacao.wav");
    }

    private class VerificarListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            processarResposta();
        }
    }

    private class GithubListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            abrirGitHubNoNavegador();
        }

        private void abrirGitHubNoNavegador() {
            try {
                URI githubURI = new URI("https://github.com/alexandrelorena/projetos-em-java.git");
                java.awt.Desktop.getDesktop().browse(githubURI);
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void playSound(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}


